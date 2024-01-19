## Overview
- [x] sending scheduled image each day to messenger webhook
    - [x] storing images in db, has a field to add which day image will be sent
    - [x] storing subscribers for which the image will be transmitted to
    - [x] if in db image with current date -> send this image, send random image that has not been sent yet
- [x] CRUD for adding and deleting images from db
- [ ] check if API key is correct (for only owner can edit db) - security
- [x] docker it and host on hosting service to actually run

## API design backend
- CRUD module: 
  - get list of all images - paginate and filter by date, title OR get list of all image titles and thumbnails to filter on frontend side
  - upload new image with title and scheduled date
  - delete image(s) with given title or scheduled for given day(s)
  - update image with given id or with given title
- Messenger bot module:
  - endpoint that is executed in a fixed schedule, make call to database to check for scheduled images and pass the list to messenger webhook integration method
    - consider edge case where user supplies an image and the scheduled task is run before the image upload to database -> add scheduling for minimum of 2 minutes in the future from current date? that way scheduled task is run at least once (given 1 minute scheduled time)
  - messenger webhook integration method that uses http client in order to send messages to a webhook

## To implement
**Image:**
- [ ] Change swagger params json schema to camelCase
- [ ] Get one image details by ID
- [x] Thumbnails endpoint that sends thumbnail version of the image
  - [x] Generate thumbnail only if provided width and height is less than the original image
- [x] Delete multiple -> send only image id array to backend, send only scheduled message id
- [ ] Search by image name
- [ ] Refactor exception structure
- [ ] Image info and scheduled message pagination
- [ ] Security with JWT