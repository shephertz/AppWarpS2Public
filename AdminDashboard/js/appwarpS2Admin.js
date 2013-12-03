var AppWarp = AppWarp || {};
AppWarp.WarpClient = AppWarp.WarpClient || {};
AppWarp.WarpClient.Admin = AppWarp.WarpClient.Admin || {};
AppWarp.Utility = AppWarp.Utility || {};
AppWarp.RequestType = AppWarp.RequestType || {};
AppWarp.PayloadType = AppWarp.PayloadType || {};
AppWarp.MessageType = AppWarp.MessageType || {};
AppWarp.Response = AppWarp.Response || {};

AppWarp.RequestType = 
{
	Auth : 1,
	JoinLobby : 2,
	SubscribeLobby : 3,   
	UnsubscribeLobby : 4,    
	LeaveLobby : 5,    
	CreateRoom : 6,    
	JoinRoom : 7,    
	SubscribeRoom : 8,    
	UnsubscribeRoom : 9,    
	LeaveRoom : 10,    
	DeleteRoom : 11,    
	Chat : 12,    
	UpdatePeers : 13,    
	Signout : 14,
	CreateZone : 15,
	DeleteZone : 16,  
	GetRooms : 17,
	GetUsers : 18,
	GetUserInfo : 19,
	GetRoomInfo : 20,
	SetCustomRoomData : 21,
	SetCustomUserData : 22,        
	GetLobbyInfo : 23,

	JoinRoomWithNUser : 24,
	UpdateRoomProperty : 25,
	JoinRoomWithProperties : 27,
	GetRoomWithNUser : 28,
	GetRoomWithProperties : 29,
	JoinRoomInRange : 37,
	GetRoomInRange : 38,

	LockProperties : 35,
	UnlockProperties : 36,
	KeepAlive : 63,
	PrivateChat : 30,
	Move : 31,

	GetZones : 59,
	ValidateAdminCredentials : 60,
	GetLiveStats : 61
};

AppWarp.PayloadType = {
	FlatString : 0,    
	Binary : 1,   
	Json : 2
};

AppWarp.MessageType = {
    Request : 0,
    Response : 1,
    Update : 2
};

AppWarp.ResultCode = {
   	Success : 0, 	
	AuthError : 1,    	
	ResourceNotFound : 2,    	
	ResourceMoved : 3,     	
	BadRequest : 4,	
	ConnectionError : 5,	
	UnknownError : 6,
	ResultSizeError : 7,
	ApiNotFound : 8
};

AppWarp.Utility = (function(){
	return{
		hex2bin : function(hex){
		    var bytes = [], str;

		    for(var i=0; i< hex.length-1; i+=2)
		        bytes.push(parseInt(hex.substr(i, 2), 16));

		    return String.fromCharCode.apply(String, bytes);    
		},

		bytesToIntger : function(bytes, offset){
			var value = 0;
			for (var i = 0; i < 4; i++)
			{
				value = (value << 8) + (bytes[offset + i] & 0xff);
			}  

			return value;
		},

		bin2String : function(array) {
			var str = "";
			for(var i = 0; i < array.length; i ++) {
				var char = array[i];
				str += String.fromCharCode(char);
			}
			return str;
		},

		base64_encode : function(data) {
			  var b64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
			  var o1, o2, o3, h1, h2, h3, h4, bits, i = 0,
			    ac = 0,
			    enc = "",
			    tmp_arr = [];

			  if (!data) {
			    return data;
			  }

			  do { // pack three octets into four hexets
			    o1 = data.charCodeAt(i++);
			    o2 = data.charCodeAt(i++);
			    o3 = data.charCodeAt(i++);

			    bits = o1 << 16 | o2 << 8 | o3;

			    h1 = bits >> 18 & 0x3f;
			    h2 = bits >> 12 & 0x3f;
			    h3 = bits >> 6 & 0x3f;
			    h4 = bits & 0x3f;

			    // use hexets to index into b64, and append result to encoded string
			    tmp_arr[ac++] = b64.charAt(h1) + b64.charAt(h2) + b64.charAt(h3) + b64.charAt(h4);
			  } while (i < data.length);

			  enc = tmp_arr.join('');

			  var r = data.length % 3;
			  return (r ? enc.slice(0, r - 3) : enc) + '==='.slice(r || 3);
		},

		getODataUTCDateFilter : function(){
			var date = new Date();
			var monthString;
			var rawMonth = (date.getUTCMonth()+1).toString();
			if (rawMonth.length == 1) {
				monthString = "0" + rawMonth;
			}
			else
			{
				monthString = rawMonth;
			}

			var dateString;
			var rawDate = date.getUTCDate().toString();
			if (rawDate.length == 1) {
				dateString = "0" + rawDate;
			}
			else
			{
				dateString = rawDate;
			}
			//var DateFilter = "datetime\'";
			var DateFilter = "";
			DateFilter += date.getUTCFullYear() + "-";
			DateFilter += monthString + "-";
			DateFilter += dateString;
			DateFilter += "T" + date.getUTCHours() + ":";
			DateFilter += date.getUTCMinutes() + ":";
			DateFilter += date.getUTCSeconds() + ".";
			DateFilter += date.getUTCMilliseconds();
			DateFilter += "Z";
			return DateFilter;
		},

		aesDecrypt: function(text, passPhrase){
			var iv = "F27D5C9927726BCEFE7510B1BDD3D137";
			var salt = "3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55";
			var keySize = 128;
			var iterations = iterationCount = 10;
			var aesUtil = new AesUtil(keySize, iterationCount)
			return aesUtil.decrypt(salt, iv, passPhrase, text);
		},

		aesEncrypt: function(text, passPhrase){
			var iv = "F27D5C9927726BCEFE7510B1BDD3D137";
			var salt = "3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55";
			var keySize = 128;
			var iterations = iterationCount = 10;
			var aesUtil = new AesUtil(keySize, iterationCount)
			return aesUtil.encrypt(salt, iv, passPhrase, text);;
		}
	}
})();

AppWarp.Response = (function(){
		var Response = function(responseBytes, startIndex) {
            this.messageType = responseBytes[startIndex + 0];
            this.requestType = responseBytes[startIndex + 1];
            this.resultCode = responseBytes[startIndex + 2];
            this.reserved = responseBytes[startIndex + 3];
            this.payLoadType = responseBytes[startIndex + 4];
            this.payLoadSize = AppWarp.Utility.bytesToIntger(responseBytes, startIndex + 5);
            this.payLoad = new Uint8Array(this.payLoadSize);
            for (var i = 0; i < this.payLoadSize; i++) {
                this.payLoad[i] = responseBytes[9 + startIndex + i];
            }
        }
        Response.prototype.getMessageType = function () {
            return this.messageType;
        };

        Response.prototype.getRequestType = function () {
            return this.requestType;
        };

        Response.prototype.getResultCode = function () {
            return this.resultCode;
        };

        Response.prototype.getPayloadType = function () {
            return this.payLoadType;
        };

        Response.prototype.getPayloadSize = function () {
            return this.payLoadSize;
        };

        Response.prototype.getPayload = function () {
            return this.payLoad;
        };

        Response.prototype.getPayloadString = function () {
            return AppWarp.Utility.bin2String(this.payLoad);
        };

        Response.prototype.getPayloadStringDec = function (password) {
            return AppWarp.Utility.aesDecrypt(AppWarp.Utility.bin2String(this.payLoad),password);
        };

        return Response;
})();

AppWarp.WarpClient.Admin = (function(){

	var buildWarpRequest = function(AppWarpSessionId, requestType, payload, isText){
			// Construct binary warp request
			var bytearray = new Uint8Array(16+payload.length);
			bytearray[0] = AppWarp.MessageType.Request;	// message type: request
			bytearray[1] = requestType;	// request type

			// bytes 2-5 : session id
			bytearray[2] = AppWarpSessionId>>>24;
			bytearray[3] = AppWarpSessionId>>>16;
			bytearray[4] = AppWarpSessionId>>>8;
			bytearray[5] = AppWarpSessionId;
			// bytes 6-9 : request id
			for(var i=6; i<=9; i++){
				bytearray[i] = 0;
			}
			// byte 10: reserved
			bytearray[10] = 1;

			// byte 11 : payload type String, Binary, JSON
			if(payload.length > 0 && requestType != AppWarp.RequestType.UpdatePeers){
				bytearray[11] = AppWarp.PayloadType.Json;	
			}
			else{
				bytearray[11] = AppWarp.PayloadType.Binary;	
			}

			// byte 12-15: payload size
			var payloadSize = payload.length;
			bytearray[12] = payloadSize>>>24;
			bytearray[13] = payloadSize>>>16;
			bytearray[14] = payloadSize>>>8;
			bytearray[15] = payloadSize;

			// bytes 16 onwards : actual payload	
			if(isText == false){
				for (var i = 0; i < payloadSize; ++i)
				{
					bytearray[16+i] = payload[i];
				}
			}
			else{
				for (var i = 0; i < payloadSize; ++i)
				{
					bytearray[16+i] = payload.charCodeAt(i);
				}				
			}
			return bytearray;	
		}

	var buildAdminRequest = function(username, password){

		var timeStamp = AppWarp.Utility.getODataUTCDateFilter();
		var params = "";
		params += "apiKey" + "";
		params += "timeStamp" + timeStamp;
		params += "user" + username;
		params += "version" + "Admin_1.0";

		var hmac = AppWarp.CryptoJS.HmacSHA1(params, password).toString();
		var signature = encodeURIComponent(AppWarp.Utility.base64_encode(AppWarp.Utility.hex2bin(hmac)));

		var json = {};
		json.apiKey = "";
		json.version = "Admin_1.0";
		json.timeStamp = timeStamp;
		json.user = username;
		json.signature = signature;

		return JSON.stringify(json);
	}

	var buildCreateRoomRequest = function(apiKey, name, maxUsers, properties, username, password){

		var timeStamp = AppWarp.Utility.getODataUTCDateFilter();
		var params = "";
		params += "apiKey" + apiKey;
		params += "timeStamp" + timeStamp;
		params += "user" + username;
		params += "version" + "Admin_1.0";

		var hmac = AppWarp.CryptoJS.HmacSHA1(params, password).toString();
		var signature = encodeURIComponent(AppWarp.Utility.base64_encode(AppWarp.Utility.hex2bin(hmac)));

		var json = {};
		json.apiKey = apiKey;
		json.version = "Admin_1.0";
		json.timeStamp = timeStamp;
		json.user = username;
		json.signature = signature;
		json.name = name;
		json.maxUsers = maxUsers;
		json.properties = properties;

		return JSON.stringify(json);
	}

	var buildDeleteRoomRequest = function(apiKey, id, username, password){

		var timeStamp = AppWarp.Utility.getODataUTCDateFilter();
		var params = "";
		params += "apiKey" + apiKey;
		params += "timeStamp" + timeStamp;
		params += "user" + username;
		params += "version" + "Admin_1.0";

		var hmac = AppWarp.CryptoJS.HmacSHA1(params, password).toString();
		var signature = encodeURIComponent(AppWarp.Utility.base64_encode(AppWarp.Utility.hex2bin(hmac)));

		var json = {};
		json.apiKey = apiKey;
		json.version = "Admin_1.0";
		json.timeStamp = timeStamp;
		json.user = username;
		json.signature = signature;
		json.id = id;

		return JSON.stringify(json);
	}

	var buildCreateZoneRequest = function(zone, username, password){

		var timeStamp = AppWarp.Utility.getODataUTCDateFilter();
		var params = "";
		params += "apiKey" + "";
		params += "timeStamp" + timeStamp;
		params += "user" + username;
		params += "version" + "Admin_1.0";

		var hmac = AppWarp.CryptoJS.HmacSHA1(params, password).toString();
		var signature = encodeURIComponent(AppWarp.Utility.base64_encode(AppWarp.Utility.hex2bin(hmac)));

		var json = {};
		json.apiKey = "";
		json.version = "Admin_1.0";
		json.timeStamp = timeStamp;
		json.user = username;
		json.signature = signature;
		json.AppName = zone;

		return JSON.stringify(json);
	}

	var buildDeleteZoneRequest = function(apiKey, username, password){

		var timeStamp = AppWarp.Utility.getODataUTCDateFilter();
		var params = "";
		params += "apiKey" + apiKey;
		params += "timeStamp" + timeStamp;
		params += "user" + username;
		params += "version" + "Admin_1.0";

		var hmac = AppWarp.CryptoJS.HmacSHA1(params, password).toString();
		var signature = encodeURIComponent(AppWarp.Utility.base64_encode(AppWarp.Utility.hex2bin(hmac)));

		var json = {};
		json.apiKey = apiKey;
		json.version = "Admin_1.0";
		json.timeStamp = timeStamp;
		json.user = username;
		json.signature = signature;

		return JSON.stringify(json);
	}

	return {
		CreateRoom : function(apiKey, name, maxUsers, properties, username, password, host, port, callback){
			var socket = new WebSocket("ws://"+host+":"+port);
			socket.binaryType = "arraybuffer";
			socket.onopen = function(){
				var payload = buildCreateRoomRequest(apiKey, name, maxUsers, properties, username, password);
				var bytes = buildWarpRequest(0,AppWarp.RequestType.CreateRoom, AppWarp.Utility.aesEncrypt(payload, password), true);
				socket.send(bytes.buffer);
			};
			socket.onclose = function(){
			};
			socket.onmessage = function(msg){
				var bytearray = new Uint8Array(msg.data);
				var res = new AppWarp.Response(bytearray, 0);
				callback(res);
			}
		},

		CreateZone : function(zone, username, password, host, port, callback){
			var socket = new WebSocket("ws://"+host+":"+port);
			socket.binaryType = "arraybuffer";
			socket.onopen = function(){
				var payload = buildCreateZoneRequest(zone, username, password);
				var bytes = buildWarpRequest(0,AppWarp.RequestType.CreateZone, AppWarp.Utility.aesEncrypt(payload, password), true);
				socket.send(bytes.buffer);
			};
			socket.onclose = function(){
			};
			socket.onmessage = function(msg){
				var bytearray = new Uint8Array(msg.data);
				var res = new AppWarp.Response(bytearray, 0);
				callback(res);
			}
		},

		DeleteRoom : function(apiKey, id, username, password, host, port, callback){
			var socket = new WebSocket("ws://"+host+":"+port);
			socket.binaryType = "arraybuffer";
			socket.onopen = function(){
				var payload = buildDeleteRoomRequest(apiKey, id, username, password);
				var bytes = buildWarpRequest(0,AppWarp.RequestType.DeleteRoom, AppWarp.Utility.aesEncrypt(payload, password), true);
				socket.send(bytes.buffer);
			};
			socket.onclose = function(){
			};
			socket.onmessage = function(msg){
				var bytearray = new Uint8Array(msg.data);
				var res = new AppWarp.Response(bytearray, 0);
				callback(res);
			}
		},

		DeleteZone : function(apiKey, username, password, host, port, callback){
			var socket = new WebSocket("ws://"+host+":"+port);
			socket.binaryType = "arraybuffer";
			socket.onopen = function(){
				var payload = buildDeleteZoneRequest(apiKey, username, password);
				var bytes = buildWarpRequest(0,AppWarp.RequestType.DeleteZone, AppWarp.Utility.aesEncrypt(payload, password), true);
				socket.send(bytes.buffer);
			};
			socket.onclose = function(){
			};
			socket.onmessage = function(msg){
				var bytearray = new Uint8Array(msg.data);
				var res = new AppWarp.Response(bytearray, 0);
				callback(res);
			}
		},

		GetLiveStats : function(username, password, host, port, callback){
			var socket = new WebSocket("ws://"+host+":"+port);
			socket.binaryType = "arraybuffer";
			socket.onopen = function(){
				var payload = buildAdminRequest(username, password);
				var bytes = buildWarpRequest(0,AppWarp.RequestType.GetLiveStats, AppWarp.Utility.aesEncrypt(payload, password), true);
				socket.send(bytes.buffer);
			};
			socket.onclose = function(){
			};
			socket.onmessage = function(msg){
				var bytearray = new Uint8Array(msg.data);
				var res = new AppWarp.Response(bytearray, 0);
				callback(res);
			}
		},

		GetZones : function(username, password, host, port, callback){
			var socket = new WebSocket("ws://"+host+":"+port);
			socket.binaryType = "arraybuffer";
			socket.onopen = function(){
				var payload = buildAdminRequest(username, password);
				var bytes = buildWarpRequest(0,AppWarp.RequestType.GetZones, AppWarp.Utility.aesEncrypt(payload, password), true);
				socket.send(bytes.buffer);
			};
			socket.onclose = function(){
			};
			socket.onmessage = function(msg){
				var bytearray = new Uint8Array(msg.data);
				var res = new AppWarp.Response(bytearray, 0);
				callback(res);
			}
		},

		ValidateCredentials : function(username, password, host, port, callback){
			var socket = new WebSocket("ws://"+host+":"+port);
			socket.binaryType = "arraybuffer";
			socket.onopen = function(){
				var payload = buildAdminRequest(username, password);
				var bytes = buildWarpRequest(0,AppWarp.RequestType.ValidateAdminCredentials, AppWarp.Utility.aesEncrypt(payload, password), true);
				socket.send(bytes.buffer);
			};
			socket.onclose = function(){
			};
			socket.onmessage = function(msg){
				var bytearray = new Uint8Array(msg.data);
				var res = new AppWarp.Response(bytearray, 0);
				callback(res.getResultCode());
			}
		}
	}
})();