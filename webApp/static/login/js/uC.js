'use strict';
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

	vm.getAllStudents = function() {
		$http.get('/student/getAllStudents')
		.then(function success(response) {
				vm.students = response.data.students;
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
		console.log(vm.formInfo)
		console.log(vm.formInfo.email)
		
		$http.post('/loginStudent/', vm.formInfo)
			.then(function success(response) {
				console.log(response);
				toastr.success(" login successful");
				//storeUserCredentials(formInfo.email + '.yourServerToken');
				//$scope.setCurrentUsername(vm.email);
				//$rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
				//$scope.setCurrentUser(user);
				var loginData = {username: vm.formInfo.email, userRole: "student"};
				Auth.login(loginData);
				//$rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
				//var loginData = vm.formInfo["email"];
				//$window.sessionStorage["userInfo"] = JSON.stringify(loginData);
				//Session.create(loginData);
				//$rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
				
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
				//storeUserCredentials(formInfo.email + '.yourServerToken');
				var loginData = {username: vm.formInfo.email, userRole: "admin"};
				Auth.login(loginData);
				//var loginData = {username: vm.formInfo.email, userRole: "admin"};
				//Auth.login(loginData);
				$rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
				$window.location.href='/admin/dashboard/';
				//$window.location.href='/admin/dashboard/';
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


/**
app.constant('AUTH_EVENTS', {
  loginSuccess: 'auth-login-success',
  loginFailed: 'auth-login-failed',
  logoutSuccess: 'auth-logout-success',
  sessionTimeout: 'auth-session-timeout',
  notAuthenticated: 'auth-not-authenticated',
  notAuthorized: 'auth-not-authorized'
})

app.constant('USER_ROLES', {
  all: '*',
  admin: 'admin',
  student: 'student',
  guest: 'guest'
})

app.factory('AuthService', function($http, Session) {
	var authService = {};
	
  authService.login = function () {
    return $http
      .post('/login', vm.formInfo)
      .then(function (response) {
		console.log(response);
		toastr.success(" login successful");
        Session.create(res.data.id, res.data.user.id,
                       res.data.user.role);
        $window.location.href='/student/dashboard/';
		return res.data.user;
      });
  };
 
  authService.isAuthenticated = function () {
    return !!Session.userId;
  };
 
  authService.isAuthorized = function (authorizedRoles) {
    if (!angular.isArray(authorizedRoles)) {
      authorizedRoles = [authorizedRoles];
    }
    return (authService.isAuthenticated() &&
      authorizedRoles.indexOf(Session.userRole) !== -1);
  };
 
  return authService;
})

app.service('Session', function() {
	this.create = function (sessionId, userId, userRole) {
		this.id = sessionId;
		this.userId = userId;
		this.userRole = userRole;
	};
	this.destroy = function() {
		this.id = null;
		this.userId = null;
		this.userRole = null;
	};
})

app.controller('ApplicationController', function ($scope, USER_ROLES, AuthService) {
  $scope.currentUser = null;
  $scope.userRoles = USER_ROLES;
  $scope.isAuthorized = AuthService.isAuthorized;
 
  $scope.setCurrentUser = function (user) {
    $scope.currentUser = user;
  };
})

app.config(function ($stateProvider, USER_ROLES) {
	$stateProvider.state('studentDashboard', {
		url: '/student/dashboard/',
		templateUrl: 'intramural/templates/dashboard.html',
		data: {
			authorizedRoles: [USER_ROLES.admin, USER_ROLES.student]
		}
	});
})

app.run(function ($rootScope, AUTH_EVENTS, AuthService) {
  $rootScope.$on('$stateChangeStart', function (event, next) {
    var authorizedRoles = next.data.authorizedRoles;
    if (!AuthService.isAuthorized(authorizedRoles)) {
      event.preventDefault();
      if (AuthService.isAuthenticated()) {
        // user is not allowed
        $rootScope.$broadcast(AUTH_EVENTS.notAuthorized);
      } else {
        // user is not logged in
        $rootScope.$broadcast(AUTH_EVENTS.notAuthenticated);
      }
    }
  });
})

app.config(function ($httpProvider) {
  $httpProvider.interceptors.push([
    '$injector',
    function ($injector) {
      return $injector.get('AuthInterceptor');
    }
  ]);
})
app.factory('AuthInterceptor', function ($rootScope, $q,
                                      AUTH_EVENTS) {
  return {
    responseError: function (response) { 
      $rootScope.$broadcast({
        401: AUTH_EVENTS.notAuthenticated,
        403: AUTH_EVENTS.notAuthorized,
        419: AUTH_EVENTS.sessionTimeout,
        440: AUTH_EVENTS.sessionTimeout
      }[response.status], response);
      return $q.reject(response);
    }
  };
})

.directive('loginDialog', function (AUTH_EVENTS) {
  return {
    restrict: 'A',
    template: '<div ng-if="visible"
                    ng-include="\'login.html\'">',
    link: function (scope) {
      var showDialog = function () {
        scope.visible = true;
      };
  
      scope.visible = false;
      scope.$on(AUTH_EVENTS.notAuthenticated, showDialog);
      scope.$on(AUTH_EVENTS.sessionTimeout, showDialog)
    }
  };
})*/