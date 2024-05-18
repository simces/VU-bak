import torch 
import json
import requests 
from torchvision import models, transforms 
from flask import Flask, jsonify, request 
from PIL import Image 
from io import BytesIO

# Loads the pretrained resnet101 model
model = models.resnet101(pretrained=True)
model.eval()

# Loads the json with class indexes
with open("imagenet_class_index.json") as indexes:
    labels_map = json.load(indexes)

# Loads the categories JSON
with open('categories.json') as f:
    categories = json.load(f)

# Flattens the JSON structure to map each tag to its hierarchical path
def flatten_categories(data, parent_path=[]):
    flat_map = {}
    for key, value in data.items():
        current_path = parent_path + [key]
        if isinstance(value, list):
            for item in value:
                flat_map[item] = current_path
        elif isinstance(value, dict):
            flat_map.update(flatten_categories(value, current_path))
    return flat_map

# Flatten the categories JSON structure
tag_to_categories = flatten_categories(categories)

# Save the flattened structure to a file
with open('flattened_categories.json', 'w') as f:
    json.dump(tag_to_categories, f, indent=2)

# Load and print the flattened structure for verification
with open('flattened_categories.json') as f:
    flattened = json.load(f)
    print(json.dumps(flattened, indent=2))

# Normalizes the passed-in photo for the model
def prepare_image(image):
    transform = transforms.Compose([
        transforms.Resize(256),
        transforms.CenterCrop(224),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),
    ])
    return transform(image).unsqueeze(0)

# Initializes the flask application
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
    top_prob, top_catid = torch.topk(probabilities, 1)
    
    # Get the top-1 prediction
    top_label = labels_map[str(top_catid.item())][1]
    top_probability = top_prob.item()
    
    # Get the hierarchical path for the top label
    hierarchical_path = tag_to_categories.get(top_label, [])
    
    result = {
        "label": top_label,
        "probability": top_probability,
        "hierarchical_path": hierarchical_path
    }
    
    return jsonify(result)

if __name__ == '__main__':
    app.run(debug=True)
