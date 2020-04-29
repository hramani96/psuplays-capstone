'use strict';

angular.module('signup')
.factory('Auth', [ '$http', '$rootScope', '$window', 'Session', 'AUTH_EVENTS',
function($http, $rootScope, $window, Session, AUTH_EVENTS) {
	var authService = {};
		authService.login = function(user) {

			//var email = user["email"];
			
			var loginData = user;
				//console.log(loginData)
				$window.sessionStorage["userInfo"] = JSON.stringify(loginData);
				//console.log("b");
				sessionStorage.setItem('username', loginData.username)
				sessionStorage.setItem('userRole', loginData.userRole)
				Session.create(loginData);
				//console.log("c");
				$rootScope.currentUser = loginData;
				//console.log("root " + $rootScope.currentUser.username + " login " + loginData.username);
				$rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
				//console.log("e");
					
	};

	//check if the user is authenticated
	authService.isAuthenticated = function() {
		return !!Session.user;
	};
	
	//check if the user is authorized to access the next route
	//this function can be also used on element level
	//e.g. <p ng-if="isAuthorized(authorizedRoles)">show this only to admins</p>
	authService.isAuthorizedAdmin = function(authorizedRoles) {
		if (!angular.isArray(authorizedRoles)) {
		//	console.log("pop");
	      authorizedRoles = [authorizedRoles];
	    }
		//console.log("auth # " + authorizedRoles.indexOf(Session.userRole));
		//console.log("admin auth " + authorizedRoles.indexOf(Session.userRole) === 0);
		//console.log("role " + Session.userRole);
		//return (authorizedRoles.indexOf(Session.userRole) == 0);
	    return (authService.isAuthenticated() &&
	      authorizedRoles.indexOf(Session.userRole) == 0);
	};
	
	authService.isAuthorizedStudent = function(authorizedRoles) {
		if (!angular.isArray(authorizedRoles)) {
		//	console.log("pop");
	      authorizedRoles = [authorizedRoles];
	    }
		//console.log("auth # " + authorizedRoles.indexOf(Session.userRole));
		//		console.log("student auth " + authService.isAuthenticated());
		//console.log("role " + Session.userRole);
		//console.log(authorizedRoles);
		//return (authorizedRoles.indexOf(Session.userRole) == -1);
	    return (authService.isAuthenticated() &&
	      authorizedRoles.indexOf(Session.userRole) == -1);
	};
	
	//log out the user and broadcast the logoutSuccess event
	authService.logout = function(){
		Session.destroy();
		$window.sessionStorage.removeItem("userInfo");
		$rootScope.$broadcast(AUTH_EVENTS.logoutSuccess);
	}

	return authService;
	
	
} ]);