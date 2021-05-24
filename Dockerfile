### STAGE 1 - FRONTEND
# Build frontend, for react or angular choose node base image!! See e.g. https://malcoded.com/posts/angular-docker/ 
FROM busybox as frontend-docker
FROM node:12.4.0-alpine as build
WORKDIR /frontend
COPY frontend/* ./
RUN npm install
RUN npm run build

### STAGE 2 - BACKEND
#Build backend
FROM maven:3.8-openjdk-16-slim as backend-docker

# copy the project files
COPY backend/pom.xml ./pom.xml

# build all dependencies
RUN mvn dependency:go-offline -B

# copy your other files
COPY backend/src ./src

# build for release
RUN mvn package

### STAGE 3 - collect build artifacts from frontend and backend in initContainer
# create initContainer, copy from frontend and backend
FROM busybox 

# set source directory for frontend
WORKDIR /source/backend
# copy over the built artifact from the maven image
COPY --from=backend-docker target/* ./


# set source directory for backend
WORKDIR /source/frontend
# copy over the built artifact from the frontend image
COPY --from=frontend-docker frontend/* ./

WORKDIR /source