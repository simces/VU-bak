import torch
from torchvision.models import resnet101, ResNet101_Weights
from torchvision import transforms

from PIL import Image
import numpy as np

# Load the pre-trained ResNet-101 model
model = resnet101(weights=ResNet101_Weights.IMAGENET1K_V1)

# Set the model to evaluation mode
model.eval()


def prepare_image(image_path):
    transform = transforms.Compose([
        transforms.Resize(256),
        transforms.CenterCrop(224),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),
    ])

    image = Image.open(image_path).convert("RGB")
    return transform(image).unsqueeze(0)

# Example usage
img_path = 'testImages/game.png'  # Path to your test image
img_tensor = prepare_image(img_path)

with torch.no_grad():
    outputs = model(img_tensor)

# Apply softmax to convert logits to probabilities
probabilities = torch.nn.functional.softmax(outputs[0], dim=0)

# Get the top 5 categories
top5_prob, top5_catid = torch.topk(probabilities, 3)

import json

# Load class labels
with open("imagenet_class_index.json") as f:
    labels_map = json.load(f)

for i in range(top5_prob.size(0)):
    label = labels_map[str(top5_catid[i].item())]
    probability = top5_prob[i].item()
    print(f"{i + 1}: {label} ({probability:.2f})")
