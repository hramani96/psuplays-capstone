angular.module('signup').config(['$stateProvider', '$urlRouterProvider', 'USER_ROLES' ,
function($stateProvider, $urlRouterProvider, USER_ROLES) {

  // For any unmatched url, redirect to /
  $urlRouterProvider.otherwise("/");
  
  // Now set up the states
  $stateProvider
	/**.state('login', {
      url: "/",
      templateUrl: "intramural/templates/login.html",
	  
      data: {
          authorizedRoles: [USER_ROLES.admin, USER_ROLES.student]
      }
    })*/
	.state('signup', {
      url: "/signup/",
      templateUrl: "intramural/templates/signup.html",
	  
      data: {
          authorizedRoles: [USER_ROLES.all]
      }
    })
  	.state('student.dashboard', {
      url: "/student/dashboard",
      templateUrl: "intramural/templates/studentDashboard.html",
	  
      data: {
          authorizedRoles: [USER_ROLES.admin, USER_ROLES.student]
      }
    })
  	.state('admin.dashboard', {
      url: "/admin/dashboard",
      templateUrl: "adminDashboard.html",
	  data: {
          authorizedRoles: [USER_ROLES.admin]
      }      	  
    })
    ;
}]);
