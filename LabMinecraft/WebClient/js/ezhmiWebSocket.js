// Copyright (C) 2017 Hes-so Valais//Wallis, HEI, Sion. All rights reserved.
//
// Author: Patrice Rudaz (poatrice.rudaz@hevs.ch)

// *****  *************************************************************
var wsocket;

function connect() {         
	wsocket = new WebSocket("ws://localhost:8888");       
	wsocket.onmessage = onMessage;
	wsocket.onOpen    = onOpen;
}

function onMessage(evt) {             
	var str = evt.data;
	if (str.startsWith("Welcome")) {
		document.getElementById("wsMessage").innerHTML=str;
	} else {
		if (str.includes("=")) {
			var cmds     = str.split("=");
			var cmdLabel = cmds[0];
			var cmdValue = cmds[1];
			var factor   = 10;
			var offset   = 0;
			var elem     = document.getElementById(cmdLabel);
			
			if (cmdLabel.includes("GRID_U_") || cmdLabel.includes("BAR_U_")) {
				factor = 1000;
			} else if (cmdLabel.includes("_U_")) {
				factor = 300;
			} else if (cmdLabel.includes("BATT_P")) {
				factor = 6000;
				offset = -3000;
			} else if (cmdLabel.includes("_P_")) {
				factor = 5000;
			} else if (cmdLabel.includes("BATT_CHRG")) {
				factor = 100;
			} else if (cmdLabel.includes("_SW") && elem != null) {
				elem.checked = (cmdValue == "true");
				return;
			}
			
			if (elem != null) {
				elem.value=((parseFloat(cmdValue) * factor) + offset).toFixed( 2 );
			}
		}
	}
}

function onOpen() {

}

function toggleSwitch(cb) {
	if (cb != null) {
		var label = cb.id;
		var value = cb.checked;
		wsocket.send(label + "=" + value);
	}
}

