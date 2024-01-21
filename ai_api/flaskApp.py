import torch
import json
from torchvision import models, transforms
from flask import Flask, jsonify, request
from PIL import Image


# loads the pretrained resnet101 model
model = models.resnet101(pretrained = True)
model.eval()


# loads the json with class indexes
with open("imagenet_class_index.json") as indexes:
    labels_map = json.load(indexes)


# normalizes the passed in photo for the model
def prepare_image(image):
    transform = transforms.Compose([
        transforms.Resize(256),
        transforms.CenterCrop(224),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),
    ])
    return transform(image).unsqueeze(0)


# initializes the flask application
app = Flask(__name__)


# defines the endpoint
@app.route('/predict', methods = ['POST'])
def predict():
    # empty request
    if 'file' not in request.files:
        return jsonify({'error': 'No file in the request!'})
    
    # retrives the file from the request, if no file throws an error
    file = request.files['file']
    if file.filename == '':
        return jsonify({'error': 'No selected file'})
    
    # if file is there, opens the image, converts to rgb, prepares it for the model
    if file:
        image = Image.open(file.stream).convert("RGB")
        img_tensor = prepare_image(image)

        # the prepared image is then passed on to the model
        with torch.no_grad():
            outputs = model(img_tensor)

        # retrieves top 5 results with propabilities
        probabilities = torch.nn.functional.softmax(outputs[0], dim=0)
        top5_prob, top5_catid = torch.topk(probabilities, 1)
        results = [{"label": labels_map[str(catid.item())][1], "probability": prob.item()} for prob, catid in zip(top5_prob, top5_catid)]
        
        # returns the result as a JSON response
        return jsonify(results)


# runs the flask app
if __name__ == '__main__':
    app.run(debug=True)