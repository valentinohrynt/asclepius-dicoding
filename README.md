# Asclepius (Cancer Detection Android Application) - The Dicoding's Machine Learning Implementation in Android Course Proejct

## Overview
This application aims to detect skin cancer using machine learning, allowing users to upload images which are then classified as either cancerous or non-cancerous using a provided TensorFlow Lite model.

## Core Features

### Image Selection
- Gallery integration for image selection
- Image preview functionality
- **Optional:** Image cropping and rotation features using uCrop

### Cancer Detection
- Integration with provided TensorFlow Lite model
- Binary classification (cancer vs. non-cancer)
- Display of confidence scores
- Error handling and feedback

### Results Display
- **Dedicated `ResultActivity`** to show:
  - Classification result (cancer/non-cancer)
  - Confidence score
  - Preview of analyzed image
  - Error information if processing fails

## Technical Requirements

### Base Project
- Must use the provided starter project
- Maintain existing layout IDs

### Machine Learning Integration
- Use standalone TensorFlow Lite
- Implement provided cancer classification model
- Process static images from gallery

## Optional Enhancements

### Image Processing
- Image cropping functionality
- Image rotation capabilities
- Integration with uCrop library

### History Feature
- Local database integration (Room/Realm)
  - Store prediction history:
    - Images
    - Results
    - Confidence scores
- **Dedicated history view screen**

### Health Information Integration
- Integration with health news API
  - Display cancer-related articles
  - Show image, title, and description
  - API endpoint: [newsapi.org health category](https://newsapi.org/docs/endpoints/everything)

### UI/UX Improvements
- Custom layout design
- Multiple typography styles (minimum 2)
- Proper layout spacing and margins
- Harmonious color scheme
- Appropriate component usage

## Testing Requirements
- Test using provided sample images
- Verify proper image processing
- Ensure accurate model integration
- Test error handling scenarios

The application should provide a seamless experience for users to detect potential skin cancer through image analysis while maintaining high standards for user interface design and functionality.
