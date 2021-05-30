# FRONTEND
FROM node:16.2.0 as frontend-docker
WORKDIR /frontend
ENV PATH /frontend/node_modules/.bin:$PATH
COPY frontend/package.json ./
COPY frontend/package-lock.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

# BACKEND
FROM maven:3.8-openjdk-16-slim as backend-docker
COPY backend/pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
COPY backend/src ./src
RUN mvn -Dmaven.test.skip=true package

### COLLECT AND BUILD
FROM busybox 
WORKDIR /source/backend
COPY --from=backend-docker target/ ./
WORKDIR /source/frontend
COPY --from=frontend-docker frontend/build/ ./
WORKDIR /source