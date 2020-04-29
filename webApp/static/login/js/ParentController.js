'use strict';
var app = angular.module('signup')

app.config(['$httpProvider', function($httpProvider) {
	$httpProvider.defaults.xsrfCookieName = 'csrftoken';
	$httpProvider.defaults.xsrfHeaderName = 'X-CSRFToken';
}]);

app.controller('ParentController', ['$http', '$window', '$scope', '$rootScope', '$modal', 'Auth', 'AUTH_EVENTS','USER_ROLES', 'Session',
function($http, $window, $scope, $rootScope, $modal,  Auth, AUTH_EVENTS, USER_ROLES, Session){
	// this is the parent controller for all controllers.
	// Manages auth login functions and each controller
	// inherits from this controller	

	//var score1 = 0;
	//var score2 = 0;
	var vm = this;
	//$scope.modalShown = false;
	var showLoginDialog = function() {
		$window.location.href = '/signup/';
	};
	
		//$scope.modalShown = false;
	/**var showCreateGameDialog = function() {
		if(!$scope.modalShown){
			$scope.modalShown = true;
			var modalInstance = $modal.open({
				templateUrl : 'createGame.html',
				controller : "LoginCtrl",
				backdrop : 'static',
			});

			modalInstance.result.then(function() {
				$scope.modalShown = false;
			});
		}
	};*/
	
	var setCurrentUser = function(){
		//console.log("setting user " + $rootScope.currentUser.username);
		//console.log(currentUser);
		$scope.currentUser = $rootScope.currentUser;
		//console.log("retain: " + $scope.currentUser);
	}
	
	var showNotAuthorized = function(){
		alert("Not Authorized");
	}
	vm.getAllUsers = function() {
		$http.get('/getAllUsers')
			.then(function success(response) {
				vm.users = response.data.users;
			}, function error(response) {
				console.log(response);
				toastr.error("Failure : " + response.data.reason);
			});
	}
	
	vm.getName = function() {
		//console.log("sessions: ", Session.user, Session.userRole)
		//console.log("logindata", Session.loginData)
		//console.log("userinfo: ", $window.sessionStorage["userInfo"])
		//console.log($window.sessionStorage["username"])
		//console.log($window.sessionStorage["userInfo"].username)
		var account = {email: $window.sessionStorage["username"], role: $window.sessionStorage["role"]}
		console.log("acc: ", account.email)
		$http.get('/getName')
			.then(function success(response) {
				console.log(response);
				var name = response.data.name;
			}, function error(response){
				console.log(response);
			});
	}
	
	/**
	vm.getName = function() {
		//console.log("sessions: ", Session.user, Session.userRole)
		//console.log("logindata", Session.loginData)
		//console.log("userinfo: ", $window.sessionStorage["userInfo"])
		//console.log($window.sessionStorage["username"])
		//console.log($window.sessionStorage["userInfo"].username)
		var account = {email: $window.sessionStorage["username"], role: $window.sessionStorage["role"]}
		console.log("acc: ", account.email)
		$http.get('/getName')
			.then(function success(response) {
				console.log(response);
				var name = response.data.name;
			}, function error(response){
				console.log(response);
			});
	}*/
	
	//$interval($scope.getScore, 5000);
	
	//console.log("key " + $window.sessionStorage["userInfo"]);
	//$scope.currentUser = $scope.currentUser;
	$scope.currentUser = null;
	$scope.userRoles = USER_ROLES;
	$scope.isAuthorizedStudent = Auth.isAuthorizedStudent;
	$scope.isAuthorizedAdmin = Auth.isAuthorizedAdmin;


	//listen to events of unsuccessful logins, to run the login dialog
	$rootScope.$on(AUTH_EVENTS.notAuthorized, showNotAuthorized);
	$rootScope.$on(AUTH_EVENTS.notAuthenticated, showLoginDialog);
	$rootScope.$on(AUTH_EVENTS.sessionTimeout, showLoginDialog);
	$rootScope.$on(AUTH_EVENTS.logoutSuccess, showLoginDialog);
	$rootScope.$on(AUTH_EVENTS.loginSuccess, setCurrentUser);
	
	
	//spyOn(rootScope,'$broadcast').andCallThrough();
	
} ]);