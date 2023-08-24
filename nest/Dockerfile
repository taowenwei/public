FROM node:16

WORKDIR /app
EXPOSE 3000

COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN npm run build

CMD [ "node", "dist/main.js" ]