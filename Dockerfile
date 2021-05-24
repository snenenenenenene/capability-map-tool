### STAGE 1 - FRONTEND
# Build frontend, for react or angular choose node base image!! See e.g. https://malcoded.com/posts/angular-docker/ 
FROM busybox as frontend-docker
FROM node:13.12.0-alpine
WORKDIR /frontend
ENV PATH /app/node_modules/.bin:$PATH
# copy contents of frontend dir. For react or angular run npm build or equivalent
COPY frontend/package.json ./
COPY frontend/package-lock.json ./
RUN npm install --silent
RUN npm install react-scripts@3.4.1 -g --silent

COPY frontend/* ./

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