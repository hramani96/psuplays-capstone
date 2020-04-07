'use strict';

angular.module('signup').
controller('ParentController', ['$window', '$scope', '$rootScope', '$modal', 'Auth', 'AUTH_EVENTS','USER_ROLES',
function($window, $scope, $rootScope, $modal, Auth, AUTH_EVENTS, USER_ROLES){
	// this is the parent controller for all controllers.
	// Manages auth login functions and each controller
	// inherits from this controller	

	
	$scope.modalShown = false;
	var showLoginDialog = function() {
		$window.location.href = '/signup/';
	};
	
	var setCurrentUser = function(){
		console.log("setting user " + $rootScope.currentUser.username);
		//console.log(currentUser);
		$scope.currentUser = $rootScope.currentUser;
		//console.log("retain: " + $scope.currentUser);
	}
	
	var showNotAuthorized = function(){
		alert("Not Authorized");
	}
	
	//console.log("key " + $window.sessionStorage["userInfo"]);
	$scope.currentUser = null;
	$scope.userRoles = USER_ROLES;
	$scope.isAuthorized = Auth.isAuthorized;

	//listen to events of unsuccessful logins, to run the login dialog
	$rootScope.$on(AUTH_EVENTS.notAuthorized, showNotAuthorized);
	$rootScope.$on(AUTH_EVENTS.notAuthenticated, showLoginDialog);
	$rootScope.$on(AUTH_EVENTS.sessionTimeout, showLoginDialog);
	$rootScope.$on(AUTH_EVENTS.logoutSuccess, showLoginDialog);
	$rootScope.$on(AUTH_EVENTS.loginSuccess, setCurrentUser);
	
	
	//spyOn(rootScope,'$broadcast').andCallThrough();
	
} ]);