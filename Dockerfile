### STAGE 1 - FRONTEND
#Build frontend
FROM node:16.2.0 as frontend-docker
WORKDIR /frontend
ENV PATH /frontend/node_modules/.bin:$PATH
COPY frontend/package.json ./
COPY frontend/package-lock.json ./
RUN npm install
COPY frontend/ ./
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

# build for releasem 
# changed this to skip tests, would execute them, causing log to overflow
RUN mvn -Dmaven.test.skip=true package

### STAGE 3 - collect build artifacts from frontend and backend in initContainer
# create initContainer, copy from frontend and backend
FROM busybox 

# set source directory for frontend
WORKDIR /source/backend
# copy over the built artifact from the maven image
COPY --from=backend-docker target/ ./


# set source directory for backend
WORKDIR /source/frontend
# copy over the built artifact from the frontend image
# changed this to get files from the /build/ folder
COPY --from=frontend-docker frontend/build/ ./

WORKDIR /source