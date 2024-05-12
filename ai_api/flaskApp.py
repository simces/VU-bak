# C:\Python311\python.exe .\flaskApp.py   

import torch 
import json
import requests 
from torchvision import models, transforms 
from flask import Flask, jsonify, request 
from PIL import Image 
from io import BytesIO


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


@app.route('/predict', methods=['GET'])
def predict():
    # URL parameter
    image_url = request.args.get('url')
    if not image_url:
        return jsonify({'error': 'No image URL provided!'})
    
    try:
        response = requests.get(image_url)
        image = Image.open(BytesIO(response.content)).convert("RGB")
    except Exception as e:
        return jsonify({'error': 'Failed to load image from URL. Error: {}'.format(str(e))})
    
    img_tensor = prepare_image(image)
    with torch.no_grad():
        outputs = model(img_tensor)

    probabilities = torch.nn.functional.softmax(outputs[0], dim=0)
    top5_prob, top5_catid = torch.topk(probabilities, 3)
    results = [{"label": labels_map[str(catid.item())][1], "probability": prob.item()} for prob, catid in zip(top5_prob, top5_catid)]
    
    return jsonify(results)

if __name__ == '__main__':
    app.run(debug=True)