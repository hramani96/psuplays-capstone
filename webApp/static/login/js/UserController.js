var app = angular.module('signup', ['ui.router', 'ui.bootstrap']);

app.config(['$httpProvider', function($httpProvider) {
	$httpProvider.defaults.xsrfCookieName = 'csrftoken';
	$httpProvider.defaults.xsrfHeaderName = 'X-CSRFToken';
}]);

app.controller('UserController', ['$http', '$window', '$scope', '$rootScope', 'Auth', 'AUTH_EVENTS', function($http, $window, $scope, $rootScope, Auth, AUTH_EVENTS) {

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
                    $window.location.href='/admin/manageAdmin/';
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
                $window.location.href='/admin/manageAdmin/';
			}, function error(response) {
				console.log(response);
				toastr.error("Failure : " + response.data.reason);
			});
	}

	vm.login_student = function() {
		console.log(vm.formInfo)
		console.log(vm.formInfo.email)
		
		$http.post('/loginStudent/', vm.formInfo)
			.then(function success(response) {
				console.log(response);
				toastr.success(" login successful");
			
				var loginData = {username: vm.formInfo.email, userRole: "student"};
				Auth.login(loginData);
		
				
				$window.location.href='/student/dashboard/';
			},
				function error(response) {
					console.log(response);
					toastr.error("failure : " + response.data.reason);
					$rootScope.$broadcast(AUTH_EVENTS.loginFailed);
				});
	}

	vm.login_admin = function() {
		$http.post('/loginAdmin/', vm.formInfo)
			.then(function success(response) {
				console.log(response);
				toastr.success(" login successful");
				var loginData = {username: vm.formInfo.email, userRole: "admin"};
				Auth.login(loginData);
				
				$rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
				$window.location.href='/admin/dashboard/';
			},
				function error(response) {
					console.log(response);
					toastr.error("failure : " + response.data.reason);
					$rootScope.$broadcast(AUTH_EVENTS.loginFailed);
				});
	}
	
	vm.logout = function() {
		Auth.logout();
		$window.location.href='/signup/';
		
	}
	
	if ($window.sessionStorage["userInfo"]) {
		var credentials = JSON.parse($window.sessionStorage["userInfo"]);
		console.log("Page refreshed, maintained info: "+$window.sessionStorage["userInfo"]);
		console.log(credentials);
		Auth.login(credentials);
	}
	
}]);
