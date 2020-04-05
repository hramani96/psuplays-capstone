var app = angular.module('signup', []);

.service('AuthService', function($q, $http, USER_ROLES) {
	var LOCAL_TOKEN_KEY = 'yourTokenKey';
	var username = '';
	var isAuthenticated = false;
	var role = '';
	var authToken;

	function loadUserCredentials() {
		var token = window.localStorage.getItem(LOCAL_TOKEN_KEY);
		if(token){
			useCredentials(token);
		}
	}
	
	function storeUserCredentials(token) {
		window.localStorage.setItem(LOCAL_TOKEN_KEY, token);
		useCredentials(token);
	}
	
	function useCredentials(token){
		username = token.split('.')[0];
		isAuthenticated = true;
		authToken = token;
		
		if(username == 'admin'){
			role = USER_ROLES.admin
		}
		if(username == 'student'){
			role = USER_ROLES.student
		}
		if(username == 'public'){
			role = USER_ROLES.public
		}
		
		$http.defaults.headers.common['X-Auth-Token'] = token;
	}
	
	function destroyUserCredentials() {
		authToken = undefined;
		username = '';
		isAuthenticated = false;
		$http.defaults.headers.common['X-Auth-Token'] = undefined;
		window.localStorage.removeItem(LOCAL_TOKEN_KEY);
	}
	
	vm.isAuthorized = function(authorizedRoles) {
		if(!angular.isArray(authorizedRoles)) {
			authorizedRole = [authorizedRoles];
		}
		return (isAuthenticated && authorizedRoles.indexOf(role) !== -1);
	};
	
	loadUserCredentials();
	
	return{
		login: login,
		logout: logout,
		isAuthorized: isAuthorized,
		isAuthenticated: function() {return isAuthenticated;},
		username: function() {return username;},
		role: function() {return role;}
	};
})

.factory('AuthInterceptor', function ($rootScope, $q, AUTH_EVENTS) {
	return {
		responseError: function(response){
			$rootScope.$broadcast({
				401: AUTH_EVENTS.notAuthenticated,
				403: AUTH_EVENTS.notAuthorized
			}[response.status], response);
			return $q.reject(response);
		}
	};
})

.config(function ($httpProvider){
	$httpProvider.interceptor.push('AuthInterceptor');
});