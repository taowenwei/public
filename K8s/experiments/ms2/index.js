const express = require("express");
const axios = require("axios");

const app = express();
const port = 3000;

app.get("/", async (req, res) => {
  let { hostname, ip, originalUrl } = req;
  console.log(JSON.stringify({ hostname, ip, originalUrl }));

  let response = await axios.get("http://service-one.saturday:3000/");
  console.log(response.data);
  res.send("你好世界! " + response.data);
});

app.listen(port, () => {
  console.log(`Example app v4 listening on port ${port}`);
});
