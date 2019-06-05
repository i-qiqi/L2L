const YAML = require('yamljs');
const fs = require("fs");
// file为文件所在路径
var file = "lambda-functions/application.yml"
var data = YAML.parse(fs.readFileSync(file).toString());
console.log(data);  