FROM node:16

EXPOSE 3000

COPY package.json package-lock.json ./
RUN npm install
COPY . .

CMD [ "node", "index.js" ]