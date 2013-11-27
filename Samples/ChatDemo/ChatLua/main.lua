--
-- Abstract: Storyboard Chat Sample using AppWarp
--
--
-- Demonstrates use of the AppWarp API (connect, disconnect, joinRoom, subscribeRoom, chat )
--

display.setStatusBar( display.HiddenStatusBar )

local storyboard = require "storyboard"
local widget = require "widget"

-- load first scene
storyboard.gotoScene( "ConnectScene", "fade", 400 )

-- Replace these with the values from Admin dashboard of your AppWarp app
API_KEY = "d24961f0-5fc8-4b5c-8"
STATIC_ROOM_ID = "1742979328"
SERVER = "192.168.1.132"

-- create global warp client and initialize it
appWarpClient = require "AppWarp.WarpClient"
appWarpClient.initialize(API_KEY, SERVER)

--appWarpClient.enableTrace(true)

-- IMPORTANT! loop WarpClient. This is required for receiving responses and notifications
local function gameLoop(event)
  appWarpClient.Loop()
end

Runtime:addEventListener("enterFrame", gameLoop)