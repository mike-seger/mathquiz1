var countdown;
var countdownNumber;
var resultElem=document.getElementById("playform-result");
//var okElem=document.getElementById("playform-ok");
var paused;
var realSubmit;

function initVars() {
	paused=false;
	//countdown=0;
	//countdownNumber=0;
	realSubmit=false;
	resultElem=document.getElementById("playform-result");
	if(resultElem!=null) resultElem.value="";
}

function log(message){
	if(typeof console == "object"){
		console.log(message);
	}
}

function isMobileDevice() {
	var mobile = (/iphone|ipad|ipod|android|blackberry|mini|windows\sce|palm/i.test(navigator.userAgent.toLowerCase()));
	return mobile;
}

function focusInput() {
	if(isMobileDevice()) {
		return;
	}
	resultElem=document.getElementById("playform-result");
	resultElem.focus();
	resultElem.setAttribute("onblur", "document.getElementById('playform-result').focus();");
}



function countdownStart() {
	focusInput();
	countdownClear();
	resultElem=document.getElementById("playform-result");
	var countdownStartVal=parseInt(getInputValue("timeSecs"));
	countdownNumber = countdownStartVal+2;
    //if(countdown) {
    //}
    countdownTrigger();
	focusInput();
	log("Starting countdown");
}

function changeOKText (secs) {
	var okElem=document.getElementById("playform-ok");
	log("changeOKText: "+secs);
	if(okElem != null) {
		var text="OK";
		if(secs >0) {
			text+=" "+(secs)+'s';
		} else {
		}
		okElem.value=text;
	}
}

function countdownTrigger() {
	focusInput();
    if(countdownNumber > 0) {
        countdownNumber--;
        if(countdownNumber > 1) {
        	changeOKText(countdownNumber-1);
        }
        if(countdownNumber > 0) {
        	if(countdownNumber==1) {
        		markResult();
        	}
            countdown = setTimeout('countdownTrigger()', 1000);
        } else {
        	countdownClear();
        	countdownReached();
        }
    }
}

function countdownClear() {
    clearTimeout(countdown);
    countdown = false;
}

function isNumber(n) {
	return !isNaN(parseFloat(n)) && isFinite(n);
}

function getInputValue(name) {
	return document.getElementById(name).value;
}

function submit() {
	if(paused) {
		setInputValue("playform-result","");
		log("Cleared Input");
	}
//	document.getElementById('playform-ok').click();
	log("Submitting "+getInputValue("playform-result"));
	var okElem=document.getElementById("playform-ok");
	//okElem.removeAttribute("onclick");
	if(realSubmit) {
		okElem.setAttribute("onclick", "return true;");
	} else {
		okElem.setAttribute("onclick", "mojarra.ab(this,event,'click','@form','@form');return false;");
	}
	okElem.click();
	log("Submitted");
	okElem.setAttribute("onclick", "return pushbutton(this);");
	initVars();
	focusInput();
	//countdownClear();
	if(!realSubmit) {
		countdownStart();
	}
//	var formElem=document.getElementById("playform");
//	formElem.submit();
}

function countdownReached() {
	var inputValue=getInputValue("playform-result").trim();
	log("Clicking playform-ok: "+inputValue);
	changeOKText(0);
	if(!isNumber(inputValue)) {
		setInputValue("playform-result","");
	}
	if(inputValue.length>0 || paused) {
		submit();
	} else {
		paused=true;
		log("User is pausing...");
	}
}

function setInputValue(name, value) {
	var element=document.getElementById(name);
	element.value=value;
	element.textContent=value;
}

function updatePercentDisplay() {
	var element=document.getElementById("percentdisplay");
	var percent=document.getElementById("stepsPercent").value;
	if(percent == 100) {
		realSubmit=true;
	}
	element.setAttribute("style", "width: "+percent+"%");
}

function pushedOkButton() {
	changeOKText(0);
	countdownNumber=2;
	if(paused) {
		submit();
		return true;
	}
    updatePercentDisplay();
    return false;
}

function pushbutton(element) {
	var resultValue=getInputValue("playform-result");
	if(element.id) {
		if (element.id == "backspace" && !paused) {
			resultValue=resultValue.substring(0, resultValue.length-1);
		} else if (element.id == "clear" && !paused) {
			resultValue="";
		} else if (element.id == "playform-ok" && (resultValue.length>0 || countdownNumber<1)) {
			pushedOkButton();
		}	
	} else if(!paused) {
		if (resultValue.length+element.value.length <= resultElem.maxLength) {
			if(isValidKey(element.value.charCodeAt(0))) {
				resultValue+=element.value;
			}
		}
	}

	setInputValue("playform-result", resultValue);
	focusInput();
	
	return false;
}

function replaceClass(element, fromClass, toClass) {
	var theClass=element.getAttribute("class");
	element.setAttribute("class", theClass.replace(fromClass, toClass));
}

function markResult() {
	var resultValue=getInputValue("playform-result");
	var correctResultValue=getInputValue("correctresult");
	var commentElem=document.getElementById("comment");
	if(resultValue == correctResultValue) {
		replaceClass(resultElem, "equationvariable", "equationcorrect");
		//commentElem.setAttribute("class", "equationcorrect");
		commentElem.innerHTML="\u2714";
	} else {
		replaceClass(resultElem, "equationvariable", "equationwrong");
		commentElem.setAttribute("class", "equationcorrected");
		commentElem.innerHTML=correctResultValue;
	}
}

/*
if (document.layers) {
    document.captureEvents(Event.KEYDOWN);
	document.onkeydown =
    function (evt) {
        var keyCode = evt ? (evt.which ? evt.which : evt.keyCode) : event.keyCode;
    	alert("hi");
        if (keyCode == 13) { 
        	pushedOkButton();
        }
        if (keyCode == 27) { 
            //For escape.
            //Your function here.
        }
        else
            return true;
    };
}
*/
function isNumeric(code) {
	return code>=48 && code<=57;
}

function isValidKey(code) {
	if(!isNumeric(code) && code!=9 && code!=8 && code!=46) {
		return false;
	}
	if(isNumeric(code)) {
		var result=getInputValue("playform-result");
		if(result.length==0) return code!=48;
		return result.length<3 || (result=="100" && code==48);
	}
	return true;	
}

function handleKey(evt0) {
	var evt  = (evt0) ? evt0 : ((event) ? event : null);
	var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
	var code = evt.keyCode;
	if(code==0) {
		code=evt.charCode;
	}
	log(code);
	if ((code == 13) /*&& (node.type=="text")*/) 
	{ 
		if(!paused && getInputValue("playform-result")=="") {
			return false;
		}
		log("Enter key");
		pushedOkButton();
		return false; 
	}
	if(paused) {
		return false;
	}
		
	if (code >= 48 && code <= 57) {
		var value = evt.target.value;
		var newValue = value.substring(0, node.selectionStart);
		newValue += String.fromCharCode(code);
		newValue += value.substring(node.selectionEnd, node.value.length);
		return newValue <= 1000 && newValue.charAt(0) != '0';
	}
	return false;
	//return isValidKey(code);
}
document.onkeypress = handleKey;
if (typeof(window.addEventListener) != "undefined") { 
	window.addEventListener("load", countdownStart, false); 
}
else {
	window.attachEvent("onload", countdownStart); 
}
initVars();


log("loaded");

//countdownStart();