
(function($){

	var app = $.sammy('#main',function(){
		var memory = [];
		var ccu = [];
		var rooms = [];
		var x = [];
		var peakMemory = 0, peakCCU = 0, peakRooms = 0, peakTime = "";
		var countLimit = 50;

		rooms.push(0);
		ccu.push(0);
		memory.push(0);
		x.push(0);

		var params = null;

		this.use('Handlebars', 'hb');

		this.get('#/', function(){
			this.partial('templates/login.hb', function(){
				$("#host").val(document.location.hostname);
				$("#signInBtn").click(function(){
					$("#btnWrapper").html('<input type="button" class="buttonD" value="Signing In...">');
					$("#loginForm").submit();
				});
			});
		});

		this.post('#/login', function(){
			$("#loginInfo").text("Please Wait...");
			var that = this;
			AppWarp.WarpClient.Admin.ValidateCredentials(this.params.username, this.params.password, this.params.host, this.params.port, function(res){
				if(res == AppWarp.ResultCode.Success){
					$("#loginInfo").text("Logged In Successfully");
					params = that.params;
					that.redirect('#/dashboard');
				}
				else{
					$("#loginInfo").text("Invalid Username/Password");
					$("#btnWrapper").html('<input type="submit" class="button" value="Sign in" id="signInBtn">');
					$("#signInBtn").click(function(){
						$("#btnWrapper").html('<input type="button" class="buttonD" value="Signing In...">');
						$("#loginForm").submit();
					});
				}
			}, function(){
				$("#loginInfo").text("Invalid Host/Port");
				$("#btnWrapper").html('<input type="submit" class="button" value="Sign in" id="signInBtn">');
				$("#signInBtn").click(function(){
					$("#btnWrapper").html('<input type="button" class="buttonD" value="Signing In...">');
					$("#loginForm").submit();
				});
			});
		});

		this.get('#/dashboard', function(){
			if(params != null)
				this.partial('templates/dashboard.hb', function(){
					var memoryCtx = document.getElementById("memoryChart").getContext("2d");
					var ccuCtx = document.getElementById("ccuChart").getContext("2d");
					var roomsCtx = document.getElementById("roomChart").getContext("2d");
					var count = x.length;

					peakMemory = 0, peakCCU = 0, peakRooms = 0, peakTime = "";

					//setInterval(function() {
						//memory.append(new Date().getTime(), Math.random() * 100);
						//ccu.append(new Date().getTime(), Math.random() * 100);
						//rooms.append(new Date().getTime(), Math.random() * 100);
						if(AppWarp.WarpClient.Admin.isStatsRunning() == true)
						{
							AppWarp.WarpClient.Admin.StopLiveStats();
						}

						AppWarp.WarpClient.Admin.GetLiveStats(params.username, params.password, params.host, params.port, function(res){
							if(res.getResultCode() == AppWarp.ResultCode.Success){
								var data = JSON.parse(res.getPayloadStringDec(params.password));
								var dt = new Date()
								x.push('');

								memory.push((Math.round((data.used_memory / (1024*1024)*100)))/100);
								ccu.push(data.ccu);
								rooms.push( data.rooms);

								if(data.used_memory > peakMemory)
								{
									peakMemory = data.used_memory;
									peakCCU = data.ccu;
									peakRooms = data.rooms;
									peakTime = dt.toLocaleTimeString();

									$("#memoryPeak").text((Math.round((peakMemory / (1024*1024)*100)))/100);
									$("#ccuPeak").text(peakCCU);
									$("#roomsPeak").text(peakRooms);
									$("#updatePeak").text(peakTime);

								}

								$("#memoryInfo").text((Math.round((data.used_memory / (1024*1024)*100)))/100);
								$("#ccuInfo").text(data.ccu);
								$("#roomsInfo").text(data.rooms);
								$("#updateInfo").text(dt.toLocaleTimeString());

								if(count > countLimit)
								{
									x.splice(0,x.length - countLimit,'');
									memory.splice(0, memory.length - countLimit,0);
									ccu.splice(0, ccu.length - countLimit,0);
									rooms.splice(0, rooms.length - countLimit,0);
								}

								new Chart(memoryCtx).Line({
									labels : x,
									datasets : [
										{
											fillColor : "rgba(151,187,205,0.5)",
											strokeColor : "rgba(151,187,205,1)",
											pointColor : "rgba(151,187,205,1)",
											pointStrokeColor : "#fff",
											data : memory
										}
									]
								}, {animation: false, pointDot : false});

								new Chart(ccuCtx).Line({
									labels : x,
									datasets : [
										{
											fillColor : "rgba(151,187,205,0.5)",
											strokeColor : "rgba(151,187,205,1)",
											pointColor : "rgba(151,187,205,1)",
											pointStrokeColor : "#fff",
											data : ccu
										}
									]
								}, {animation: false, pointDot : false});

								new Chart(roomsCtx).Line({
									labels : x,
									datasets : [
										{
											fillColor : "rgba(151,187,205,0.5)",
											strokeColor : "rgba(151,187,205,1)",
											pointColor : "rgba(151,187,205,1)",
											pointStrokeColor : "#fff",
											data : rooms
										}
									]
								}, {animation: false, pointDot : false});
							}
							else{
								console.log("Error Getting Stats");
							}

							count +=1;
						});
					
					//}, 2000);
				});
			else
				this.$element().html('<div class="loggedWrapper">You are not logged in!!! <a href="#/">Click here to login</a></div>');
		});

		this.get('#/rooms', function(){
			if(params != null)
				this.partial('templates/rooms.hb', function(){
					var AppKeys = [];
					AppWarp.WarpClient.Admin.GetZones(params.username, params.password, params.host, params.port, function(res){
						if(res.getResultCode() == AppWarp.ResultCode.Success){
							var zones = JSON.parse(res.getPayloadStringDec(params.password));
							var html = "";
							for(var i=0; i<zones.length; ++i)
							{
								AppKeys.push(zones[i]);
								html = html + '<option value="' + zones[i].AppKey+ '">'+ zones[i].AppName + " (" + zones[i].AppKey + ")" + "</option>";
							}
							$("#appkey").html(html);
							$("#appkey2").html(html);
							$("#appkey3").html(html);
						}
					});

					$("#appkey2").change(function(){
						AppWarp.WarpClient.Admin.GetRooms($("#appkey2").val(), params.username, params.password, params.host, params.port, function(res){
							if(res.getResultCode() == AppWarp.ResultCode.Success){
								var rooms = JSON.parse(res.getPayloadStringDec(params.password));
								var html = "";
								for(var i=0; i<rooms.length; ++i)
								{
									html = html + '<option value="' + rooms[i].id+ '">'+ rooms[i].name + " (" + rooms[i].id + ")" + "</option>";
								}
								$("#roomname2").html(html);
							}
						});
					});

					$("#refresh").click(function(){
						AppWarp.WarpClient.Admin.GetRooms($("#appkey2").val(), params.username, params.password, params.host, params.port, function(res){
							if(res.getResultCode() == AppWarp.ResultCode.Success){
								var rooms = JSON.parse(res.getPayloadStringDec(params.password));
								var html = "";
								for(var i=0; i<rooms.length; ++i)
								{
									html = html + '<option value="' + rooms[i].id+ '">'+ rooms[i].name + " (" + rooms[i].id + ")" + "</option>";
								}
								$("#roomname2").html(html);
							}
						});
					});

					$("#createRoom").click(function(){
						if($("#appkey").val() == "" || $("#roomname").val() == "" || $("#maxusers").val() == ""){
							$("#log").html("Error: Not all details were specified<br/>");
						}
						else{
							AppWarp.WarpClient.Admin.CreateRoom($("#appkey").val(),$("#roomname").val(),$("#maxusers").val(),{}, params.username, params.password, params.host, params.port, function(res){
								if(res.getResultCode() == AppWarp.ResultCode.Success){
									var room = JSON.parse(res.getPayloadStringDec(params.password));
									$("#log").html("Success: Room Created<br/>"+"Room : " + room.name + "<br/>ID : "+room.id);
								}
								else{
									$("#log").html("Error: Creating Room<br/>");
								}
							});
						}
					});

					$("#deleteRoom").click(function(){
						if($("#appkey2").val() == "" || $("#roomname2").val() == ""){
							$("#log").html("Error: Not all details were specified<br/>");
						}
						else{
							AppWarp.WarpClient.Admin.DeleteRoom($("#appkey2").val(),$("#roomname2").val(), params.username, params.password, params.host, params.port, function(res){
								if(res.getResultCode() == AppWarp.ResultCode.Success){
									var room = JSON.parse(res.getPayloadStringDec(params.password));
									$("#log").html("Success: Room Deleted<br/>");
								}
								else{
									$("#log").html("Error: Deleting Room<br/>");
								}
							});
						}
					});

					$("#getRooms").click(function(){
						if($("#appkey3").val() == ""){
							$("#rooms").html("Error: Not all details were specified<br/>");
						}
						else{
							AppWarp.WarpClient.Admin.GetRooms($("#appkey3").val(), params.username, params.password, params.host, params.port, function(res){
								if(res.getResultCode() == AppWarp.ResultCode.Success){
									var rooms = JSON.parse(res.getPayloadStringDec(params.password));
									$("#rooms").html("");
									for(var i=0; i<rooms.length; ++i)
									{
										$("#rooms").html($("#rooms").html() + "<br/><strong>" + rooms[i].name + "</strong> : "+rooms[i].id  );								
									}
								}
								else{
									$("#rooms").html("Error: Getting All Room<br/>");
								}
							});
						}
					});
				});
			else
				this.$element().html('<div class="loggedWrapper">You are not logged in!!! <a href="#/">Click here to login</a></div>');
		});

		this.get('#/zones', function(){
			if(params != null)
				this.partial('templates/zones.hb', function(){
					$("#createZone").click(function(){
						if($("#appname").val() == ""){
							$("#log").html("Error: No Name Specified<br/>");
						}
						else{
							AppWarp.WarpClient.Admin.CreateZone($("#appname").val(), params.username, params.password, params.host, params.port, function(res){
								if(res.getResultCode() == AppWarp.ResultCode.Success){
									var zone = JSON.parse(res.getPayloadStringDec(params.password));
									$("#log").html("Success: App Created<br/>"+"App Name : " + zone.AppName + "<br/>AppKey : "+zone.AppKey/* + "<br/>Secret Key : "+zone.Secret*/);
									AppWarp.WarpClient.Admin.GetZones(params.username, params.password, params.host, params.port, function(res){
										if(res.getResultCode() == AppWarp.ResultCode.Success){
											var zones = JSON.parse(res.getPayloadStringDec(params.password));
											var html;
											$("#zones").html("");
											for(var i=0; i<zones.length; ++i)
											{
												$("#zones").html($("#zones").html() + "<br/><br/>"  +"App Name : " + zones[i].AppName + "<br/>AppKey : "+zones[i].AppKey/* + "<br/>Secret Key : "+zones[i].Secret*/ );
											}
										}
									});
								}
								else{
									$("#log").html("Error: Creating App<br/>");
								}
							});
						}
					});

					$("#deleteZone").click(function(){
						if($("#appkey").val() == ""){
							$("#log").html("Error: No App Key Specified<br/>");
						}
						else{
							AppWarp.WarpClient.Admin.DeleteZone($("#appkey").val(), params.username, params.password, params.host, params.port, function(res){
								if(res.getResultCode() == AppWarp.ResultCode.Success){
									$("#log").html("Success: App Deleted<br/>");

									AppWarp.WarpClient.Admin.GetZones(params.username, params.password, params.host, params.port, function(res){
										if(res.getResultCode() == AppWarp.ResultCode.Success){
											var zones = JSON.parse(res.getPayloadStringDec(params.password));
											var html;
											$("#zones").html("");
											for(var i=0; i<zones.length; ++i)
											{
												$("#zones").html($("#zones").html() + "<br/><br/>"  +"App Name : " + zones[i].AppName + "<br/>AppKey : "+zones[i].AppKey /*+ "<br/>Secret Key : "+zones[i].Secret*/ );
											}
										}
									});
								}
								else{
									$("#log").html("Error: Deleting App<br/>");
								}
							});
						}
					});

					$("#getZones").click(function(){
						AppWarp.WarpClient.Admin.GetZones(params.username, params.password, params.host, params.port, function(res){
							if(res.getResultCode() == AppWarp.ResultCode.Success){
								var zones = JSON.parse(res.getPayloadStringDec(params.password));
								var html;
								$("#zones").html("");
								for(var i=0; i<zones.length; ++i)
								{
									$("#zones").html($("#zones").html() + "<br/><br/>"  +"App Name : " + zones[i].AppName + "<br/>AppKey : "+zones[i].AppKey /*+ "<br/>Secret Key : "+zones[i].Secret*/ );
								}
							}
						});
					});
				});
			else
				this.$element().html('<div class="loggedWrapper">You are not logged in!!! <a href="#/">Click here to login</a></div>');
		});

		this.get("#/props", function(){
			if(params != null)
				this.partial('templates/properties.hb', function(){
					var AppKeys = [];
					AppWarp.WarpClient.Admin.GetZones(params.username, params.password, params.host, params.port, function(res){
						if(res.getResultCode() == AppWarp.ResultCode.Success){
							var zones = JSON.parse(res.getPayloadStringDec(params.password));
							var html = "";
							for(var i=0; i<zones.length; ++i)
							{
								AppKeys.push(zones[i]);
								html = html + '<option value="' + zones[i].AppKey+ '">'+ zones[i].AppName + " (" + zones[i].AppKey + ")" + "</option>";
							}
							$("#appkey").html(html);
							$("#appkey").trigger("change");
						}
					});

					$("#appkey").change(function(){
						AppWarp.WarpClient.Admin.GetRooms($("#appkey").val(), params.username, params.password, params.host, params.port, function(res){
							if(res.getResultCode() == AppWarp.ResultCode.Success){
								var rooms = JSON.parse(res.getPayloadStringDec(params.password));
								var html = "";
								for(var i=0; i<rooms.length; ++i)
								{
									html = html + '<option value="' + rooms[i].id+ '">'+ rooms[i].name + " (" + rooms[i].id + ")" + "</option>";
								}
								$("#roomname").html(html);
								$("#roomname").trigger("change");
							}
						});
					});

					$("#refresh").click(function(){
						AppWarp.WarpClient.Admin.GetRooms($("#appkey").val(), params.username, params.password, params.host, params.port, function(res){
							if(res.getResultCode() == AppWarp.ResultCode.Success){
								var rooms = JSON.parse(res.getPayloadStringDec(params.password));
								var html = "";
								for(var i=0; i<rooms.length; ++i)
								{
									html = html + '<option value="' + rooms[i].id+ '">'+ rooms[i].name + " (" + rooms[i].id + ")" + "</option>";
								}
								$("#roomname").html(html);
								$("#roomname").trigger("change");
							}
						});
					});
					
					$("#roomname").change(function(){
						AppWarp.WarpClient.Admin.GetLiveRoomInfo($("#appkey").val(), $("#roomname").val() ,params.username, params.password, params.host, params.port, function(res){
							if(res.getResultCode() == AppWarp.ResultCode.Success){
								var data = JSON.parse(res.getPayloadStringDec(params.password));
								$("#properties").val(data.properties);

								var html = "";
								//var ele = '<div class="controls"><input type="text" value="key" /> <input type="text" value="value" /> <button type="button" class="btn btn-default btn-lg"><span class="icon-remove"></span> </button></div><br>';
								var props = JSON.parse(data.properties);
								var key;
								for(key in props)
								{
									if(props.hasOwnProperty(key))
									{
										var ele = '<div data-type="controls" class="controls"><input data-type="key" type="text" value="'+key+'" /> <input data-type="value" type="text" value="'+props[key]+'" /> <button data-type="btnRemove" type="button" class="btn btn-default btn-lg"><span class="icon-remove"></span> </button><br><br></div>';
										html += ele;
									}
								}
		
								$("#keyValues").html(html);
								$("#keyValues").data("properties",data.properties);

								$("button[data-type|='btnRemove']").click(function(){
									$(this).parent().remove();
								});
							}
							else
							{
								$("#properties").val("");
								$("#log").html("Error: Getting Room Properties<br/>");
							}
						});
					});

					$("#updateRoom").click(function(){

						function isNumber(n) {
							return !isNaN(parseFloat(n)) && isFinite(n);
						}

						var json = {};
						var removeArr = [];
						$("div[data-type]").each(function(index){
							var key = $(this).children("input[data-type|='key']").val();
							var value = $(this).children("input[data-type|='value']").val();	
							if(key.trim() != "" && value.trim() != "")
							{
								if(isNumber(value))
									json[key] = eval(value);
								else
									json[key] = value;
							}
						});
						var orgJson = JSON.parse($("#keyValues").data("properties"));
						for(key in orgJson)
						{
							if(orgJson.hasOwnProperty(key))
							{
								if(!json.hasOwnProperty(key))
								{
									removeArr.push(key);
								}
							}
						}

						AppWarp.WarpClient.Admin.UpdateRoomProperties($("#appkey").val(), $("#roomname").val(), json, removeArr ,params.username, params.password, params.host, params.port, function(res){								
							if(res.getResultCode() == AppWarp.ResultCode.Success){
								var data = JSON.parse(res.getPayloadStringDec(params.password));
								$("#log").html("Success: Room Properties Updated<br/>");
								$("#log").html($("#log").html() + "Room: "+data.name+"<br>");
								$("#log").html($("#log").html() + "Id: "+data.id+"<br>");
								$("#log").html($("#log").html() + "Properties: "+data.properties+"<br>");
								$("#keyValues").data("properties",data.properties);
								//$("#roomname").trigger("change");
							}
							else
							{
								$("#log").html("Error: Updating Room Properties<br/>Error Result : "+res.getResultCode()+"<br>");
							}
						});
					});

					$("#addProp").click(function(){				
						var ele = '<div data-type="controls" class="controls"><input data-type="key" type="text" placeholder="Key" /> <input data-type="value" type="text" placeholder="Value" /> <button data-type="btnRemove" type="button" class="btn btn-default btn-lg"><span class="icon-remove"></span> </button><br><br></div>';
						$("#keyValues").append(ele);
						$("button[data-type|='btnRemove']").click(function(){
							$(this).parent().remove();
						});
					});
				});
			else
				this.$element().html('<div class="loggedWrapper">You are not logged in!!! <a href="#/">Click here to login</a></div>');
		});
		
		this.get('#/options', function(){
			if(params != null)
				this.partial('templates/options.hb', function(){
				
					function loadCurrentLicense(){
						AppWarp.WarpClient.Admin.QueryLicense(params.username, params.password, params.host, params.port, function(res){
							var data = JSON.parse(res.getPayloadString());
							var info = "<h4>Current License</h4>Email : " + data.email + "<br>" + "License : " + data.license_key + "<br>" + "CCUs : " + data.ccu;
							$("#licenseInfo").html(info);
							if(data.email === ""){
								$('#activate').prop('disabled', false);
								$('#deactivate').prop('disabled', true);
							}
							else{
								$('#activate').prop('disabled', true);
								$('#deactivate').prop('disabled', false)
							}
						});
					}
				
					loadCurrentLicense();
					$("#log").hide();
					$("#activate").click(function(){
						$("#log").show();
						$("#log").html("Activating License Key. Please wait...");
						AppWarp.WarpClient.Admin.UpdateLicense($("#license").val(),params.username, params.password, params.host, params.port, function(res){
							if(res.getResultCode() == AppWarp.ResultCode.Success){
								$("#log").html("License Updated");
								loadCurrentLicense();
							}else{
								$("#log").html("Unable to update license. Check your server's log for more details");
							}
						});
					});
					$("#deactivate").click(function(){
						$("#log").show();
						$("#log").html("Releasing License key. Please wait...");
						AppWarp.WarpClient.Admin.UpdateLicense("",params.username, params.password, params.host, params.port, function(res){						
							if(res.getResultCode() == AppWarp.ResultCode.Success){
								$("#log").html("License Released");
								loadCurrentLicense();
							}else{
								$("#log").html("Unable to release license. Check your server's log for more details");
							}
						});
					});
					$("#unlink").click(function(){
						$("#unlink_log").html("Unlinking License key. Please wait...");
						AppWarp.WarpClient.Admin.UnlinkLicense($("#unlink_license").val(),params.username, params.password, params.host, params.port, function(res){						
							if(res.getResultCode() == AppWarp.ResultCode.Success){
								$("#unlink_log").html("License Unlinked");
							}else{
								$("#unlink_log").html("Unable to unlink license. Check your server's log for more details");
							}
						});
					});
				});
			else
				this.$element().html('<div class="loggedWrapper">You are not logged in!!! <a href="#/">Click here to login</a></div>');
		});
	});

	$(function(){
		app.run('#/');
	});
})(jQuery);