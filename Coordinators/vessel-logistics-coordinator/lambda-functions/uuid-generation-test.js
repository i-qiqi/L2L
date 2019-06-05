var UUID = require('uuid');
// 1、uuid.v1(); -->基于时间戳生成  （time-based）
// 2、uuid.v4(); -->随机生成  (random)
// https://github.com/kelektiv/node-uuid
var business_key = 'lvc';
var ID = 'l2l:coordinators:'+business_key+':'+UUID.v1();
console.log("ID : ",ID);