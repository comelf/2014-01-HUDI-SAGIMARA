

var updateManager = {
	updateVisit : function(data, sectionID) {
		// 해당번호 검색(방문)한 수를 업데이트
		visitInfoBarManager.setData(data);
		var elVisitInfo = editor.get(sectionID);
		var elVisitInfoDetail = editor.get("#visited-graph", elVisitInfo);
		var elVisitedInfoBar = editor.getAll("#visited-graph .bar-section", elVisitInfo);
		var type = editor.resultFeatureDetector;
		editor.setStyle(elVisitInfoDetail, type, "running");
		visitInfoBarManager.executeBarAnimation(elVisitedInfoBar);
	},
	
	updateReport : function(data, sectionID) {
		// 해당번호 검색(방문)한 수를 업데이트
		visitInfoBarManager.setData(data);
		var elVisitInfo = editor.get(sectionID);
		var elVisitInfoDetail = editor.get("#report-graph", elVisitInfo);
		var elVisitedInfoBar = editor.getAll("#report-graph .bar-section", elVisitInfo);
		var type = editor.resultFeatureDetector;
		editor.setStyle(elVisitInfoDetail, type, "running");
		visitInfoBarManager.executeBarAnimation(elVisitedInfoBar);
	},
	
	setAnimation : function(state) {
		// 페이지 에니메이션을 시작시키는 함수
		var elContainer = editor.get("#container");
		//var elFooter = editor.get("footer");
		
		var type = editor.resultFeatureDetector;
		editor.setStyle(elContainer, type, state);
		//editor.setStyle(elFooter, type, state);
	}
}

var oVerificationStatus = {
	elTableBody : editor.get(".table>tbody"),
	getVerificationStatus : function(){
		
	}
};

var oNavigationElements = {
	elUlList : editor.get(".nav>ul"),
	elUserList : editor.get(".nav>ul").children[0],
	elVerificationManager : editor.get(".nav>ul").children[1],
	elIDonKnow : editor.get(".nav>ul").children[2],
	
	userListClickEvent : function(e){
		oNavigationElements.removeActiveClass();
		oNavigationElements.elUserList.setAttribute("class", "active");
		console.log(e.target);
	},
	
	verificationManagerClickEvent : function(e){
		oNavigationElements.removeActiveClass();
		oNavigationElements.elVerificationManager.setAttribute("class", "active");
		console.log(e.target);
	},
	
	iDonKnowClickEvent : function(e){
		oNavigationElements.removeActiveClass();
		oNavigationElements.elIDonKnow.setAttribute("class", "active");
		console.log(e.target);
	},
	removeActiveClass : function(){
		var index;
		for(index=0; index<this.elUlList.childElementCount; index++){
			this.elUlList.children[index].removeAttribute("class", "active");
		}
	}
	
}

var sagimaraIndex = {
		init : function() {
			// 실행시 contents영역을 계산해서 적용
			contentAreaResize();
			// Login, Register Button 리스너 장착
			
			oNavigationElements.elUserList.addEventListener("click",
					oNavigationElements.userListClickEvent, false);
			oNavigationElements.elVerificationManager.addEventListener("click",
					oNavigationElements.verificationManagerClickEvent, false);
			oNavigationElements.elIDonKnow.addEventListener("click",
					oNavigationElements.iDonKnowClickEvent, false);
			
			visitInfoBarManager.setDateSet();
			editor.playStatusFeatureDetector();
			this.requestVisitsData("visits", ".daily-visitor-graph");
			this.requestVisitsData("notify", ".daily-report-graph");
			
		},

		requestVisitsData : function(requestType, sectionID) {
			// 검색 요청 처리 및 서버와 통신
			updateManager.setAnimation("paused");
			
			var url = "/admin/data";
			var request = new XMLHttpRequest();
			var formdata = new FormData();
			var result;
			
			request.open("POST", url, true);			
			formdata.append("request", requestType);
			request.send(formdata);

			request.onreadystatechange = function() {
				if (request.readyState == 4 && request.status == 200) {
					result = utility.JSONparse(request.response);
					if(sectionID===".daily-visitor-graph"){
						updateManager.updateVisit(result,sectionID);
					}else if(sectionID===".daily-report-graph"){
						updateManager.updateReport(result,sectionID);
					}
					updateManager.setAnimation("running");
				}
			}
		}
	};

var contentAreaResize = function(){
	var width = (window.innerWidth || self.innerWidth || document.documentElement.clientWidth || document.body.clientWidth);
	var height = (window.innerHeight || self.innerHeight || document.documentElement.clientHeight || document.body.clientHeight);
	
	var contentsArea = editor.get(".contents");
	var navigationArea = editor.get(".nav");
	var navigationWidth = 200;
	var contentsWidth = width-navigationWidth;
	var containerChildrenHeight = height;

	contentsArea.style.setProperty("width", contentsWidth+"px");
	contentsArea.style.setProperty("height", containerChildrenHeight+"px");
	navigationArea.style.setProperty("height", containerChildrenHeight+"px");
} 


window.onresize = function() {
	contentAreaResize();

};

sagimaraIndex.init();