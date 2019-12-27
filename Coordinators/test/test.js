// const chinaTime = require('china-time');

// console.log(chinaTime().toString()); // 2018-02-07T04:38:00.000Z
const yaml = require('js-yaml');
var fs = require('fs')


let doc = yaml.safeLoad(fs.readFileSync('./resources/application.yaml', 'utf8'));
doc.topic_Bulletin.test = 'newGreet';
fs.writeFile('./resources/application.yaml', yaml.safeDump(doc), (err) => {
    if (err) {
        console.log(err);
    }
});