var app = angular.module('signup', []);

app.config(['$httpProvider', function($httpProvider) {
	$httpProvider.defaults.xsrfCookieName = 'csrftoken';
	$httpProvider.defaults.xsrfHeaderName = 'X-CSRFToken';
}]);

app.controller('UserController', function($http, $window, $scope, $state, AuthService) {

	var vm = this;


	vm.admins = []

	vm.admin = []

	vm.formInfo = {};

	vm.index = function() {
	}

	//vm.show = function(admin) {
	//    vm.admin = admin;
	//    console.log(vm.admin);
	//    //return vm.admin;
	//}

	vm.reset = function() {
		vm.formInfo = {};
	}

	vm.getAllAdmins = function() {
		$http.get('/admin/getAllAdmins')
			.then(function success(response) {
				vm.admins = response.data.admins;
			}, function error(response) {
				console.log(response);
				toastr.error("Failure : " + response.data.reason);
			});
	}

	vm.create = function() {
		$http.post('/user/create/', vm.formInfo)
			.then(function success(response) {
				console.log(response);
				toastr.success(" Account has been created");
				if (vm.formInfo.role = "Admin") {
					vm.getallAdmins();
					vm.reset();
				}
			},
				function error(response) {
					console.log(response);
					toastr.error("Failure : " + response.data.reason);
				});
	}

	vm.update = function() {
	}

	vm.delete = function(admin) {
		$http.post('/user/remove/', admin)
			.then(function success(response) {
				console.log(response);
				toastr.success(" Admin has been removed");
			}, function error(response) {
				console.log(response);
				toastr.error("Failure : " + response.data.reason);
			});
	}

	vm.login_student = function() {
		$http.post('/loginStudent/', vm.formInfo)
			.then(function success(response) {
				console.log(response);
				toastr.success(" login successful");
				storeUserCredentials(formInfo.email + '.yourServerToken');
				$scope.setCurrentUsername(vm.email);
				$window.location.href='/student/dashboard/';
			},
				function error(response) {
					console.log(response);
					toastr.error("failure : " + response.data.reason);
				});
	}

	vm.login_admin = function() {
		$http.post('/loginAdmin/', vm.formInfo)
			.then(function success(response) {
				console.log(response);
				toastr.success(" login successful");
				storeUserCredentials(formInfo.email + '.yourServerToken');
				$window.location.href='/admin/dashboard/';
			},
				function error(response) {
					console.log(response);
					toastr.error("failure : " + response.data.reason);
				});
	}
	
	vm.logout = function() {
		$http.post('')
		destroyUserCredentials();
	}
});

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