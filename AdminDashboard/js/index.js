
(function($){

	var app = $.sammy('#main',function(){
		var params = null;

		this.use('Handlebars', 'hb');

		this.get('#/', function(){
			this.partial('templates/login.hb');
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
					$("#loginInfo").text("Error in Logging In");
				}
			});
		});

		this.get('#/dashboard', function(){
			if(params != null)
				this.partial('templates/dashboard.hb', function(){
					var memory = new TimeSeries();
					var ccu = new TimeSeries();
					var rooms = new TimeSeries();

					setInterval(function() {
						//memory.append(new Date().getTime(), Math.random() * 100);
						//ccu.append(new Date().getTime(), Math.random() * 100);
						//rooms.append(new Date().getTime(), Math.random() * 100);

						AppWarp.WarpClient.Admin.GetLiveStats(params.username, params.password, params.host, params.port, function(res){
							if(res.getResultCode() == AppWarp.ResultCode.Success){
								var data = JSON.parse(res.getPayloadStringDec("password"));
								memory.append(new Date().getTime(), Math.floor(data.used_memory/1024));
								ccu.append(new Date().getTime(), data.ccu);
								rooms.append(new Date().getTime(), data.rooms);

								$("#memoryInfo").text(Math.floor(data.used_memory/1024));
								$("#ccuInfo").text(data.ccu);
								$("#roomsInfo").text(data.rooms);
								$("#updateInfo").text((new Date()).toLocaleTimeString());
							}
							else{
								console.log("Error Getting Stats");
							}
						});

					}, 2000);

					(function() {
						function RangeFunction(range) {
						  var min = 0;
						  var max = range.max ;
						  return {min: min, max: max};
						}

						var memoryChart = new SmoothieChart();
						memoryChart.addTimeSeries(memory, { strokeStyle: 'rgba(46, 204, 113,1.0)', fillStyle: 'rgba(46, 204, 113,0.2)', lineWidth: 2 });
						memoryChart.streamTo(document.getElementById("memoryChart"), 1000);

						var ccuChart = new SmoothieChart({ yRangeFunction:RangeFunction});
						ccuChart.addTimeSeries(ccu, { strokeStyle: 'rgba(52, 152, 219,1.0)', fillStyle: 'rgba(52, 152, 219,0.2)', lineWidth: 2 });
						ccuChart.streamTo(document.getElementById("ccuChart"), 1000);

						var roomsChart = new SmoothieChart({ yRangeFunction:RangeFunction});
						roomsChart.addTimeSeries(rooms, { strokeStyle: 'rgba(231, 76, 60,1.0)', fillStyle: 'rgba(231, 76, 60,0.2)', lineWidth: 2 });
						roomsChart.streamTo(document.getElementById("roomChart"), 1000);
					})();
				});
			else
				this.$element().html('You are not logged in!!! <a href="#/">Click here to login</a>');
		});

		this.get('#/rooms', function(){
			if(params != null)
				this.partial('templates/rooms.hb', function(){
					$("#createRoom").click(function(){
						if($("#appkey").val() == "" || $("#roomname").val() == "" || $("#maxusers").val() == ""){
							$("#log").html("Error: Not all details were specified<br/>");
						}
						else{
							AppWarp.WarpClient.Admin.CreateRoom($("#appkey").val(),$("#roomname").val(),$("#maxusers").val(),{}, params.username, params.password, params.host, params.port, function(res){
								if(res.getResultCode() == AppWarp.ResultCode.Success){
									var room = JSON.parse(res.getPayloadStringDec("password"));
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
									var room = JSON.parse(res.getPayloadStringDec("password"));
									$("#log").html("Success: Room Deleted<br/>");
								}
								else{
									$("#log").html("Error: Deleting Room<br/>");
								}
							});
						}
					});
				});
			else
				this.$element().html('You are not logged in!!! <a href="#/">Click here to login</a>');
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
									var zone = JSON.parse(res.getPayloadStringDec("password"));
									$("#log").html("Success: App Created<br/>"+"App : " + zone.AppName + "<br/>AppKey : "+zone.AppKey + "<br/>Secret Key : "+zone.Secret);
									AppWarp.WarpClient.Admin.GetZones(params.username, params.password, params.host, params.port, function(res){
										if(res.getResultCode() == AppWarp.ResultCode.Success){
											var zones = JSON.parse(res.getPayloadStringDec("password"));
											var html;
											$("#zones").html("");
											for(var i=0; i<zones.length; ++i)
											{
												$("#zones").html($("#zones").html() + "<br/><br/>"  +"App : " + zones[i].AppName + "<br/>AppKey : "+zones[i].AppKey + "<br/>Secret Key : "+zones[i].Secret );
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
											var zones = JSON.parse(res.getPayloadStringDec("password"));
											var html;
											$("#zones").html("");
											for(var i=0; i<zones.length; ++i)
											{
												$("#zones").html($("#zones").html() + "<br/><br/>"  +"App : " + zones[i].AppName + "<br/>AppKey : "+zones[i].AppKey + "<br/>Secret Key : "+zones[i].Secret );
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
								var zones = JSON.parse(res.getPayloadStringDec("password"));
								var html;
								$("#zones").html("");
								for(var i=0; i<zones.length; ++i)
								{
									$("#zones").html($("#zones").html() + "<br/><br/>"  +"App : " + zones[i].AppName + "<br/>AppKey : "+zones[i].AppKey + "<br/>Secret Key : "+zones[i].Secret );
								}
							}
						});
					});
				});
			else
				this.$element().html('You are not logged in!!! <a href="#/">Click here to login</a>');
		});
	});

	$(function(){
		app.run('#/');
	});
})(jQuery);