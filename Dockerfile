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

COPY backend/pom.xml ./pom.xml

RUN mvn dependency:go-offline -B

COPY backend/src ./src

RUN mvn -Dmaven.test.skip=true package

### STAGE 3 - collect build artifacts from frontend and backend in initContainer
FROM busybox 

# set source directory for frontend
WORKDIR /source/backend
COPY --from=backend-docker target/ ./


# set source directory for backend
WORKDIR /source/frontend# changed this to get files from the /build/ folder
COPY --from=frontend-docker frontend/build/ ./

WORKDIR /source