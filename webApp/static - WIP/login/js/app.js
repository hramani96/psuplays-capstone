.config(function ($stateProvider, $urlRouterProvider, USER_ROLES) {
  $stateProvider
  .state('login', {
    url: '',
    templateUrl: 'templates/login.html',
    controller: 'UserController'
  })
  .state('student.dash', {
    url: 'student/dashboard',
    views: {
        'dash-tab': {
          templateUrl: 'templates/studentDashboard.html',
          controller: 'UserController'
        }
    },
	data: {
      authorizedRoles: [USER_ROLES.student]
    }
  })
  .state('admin.dash', {
    url: 'admin/dashboard',
    views: {
        'admin-tab': {
          templateUrl: 'templates/adminDashboard.html'
        }
    },
    data: {
      authorizedRoles: [USER_ROLES.admin]
    }
  });
  
  $urlRouterProvider.otherwise(function ($injector, $location) {
    var $state = $injector.get("$state");
    $state.go("login");
  });
})

.run(function($httpBackend){
  $httpBackend.whenGET('http://localhost:8100/valid')
        .respond({message: 'This is my valid response!'});
  $httpBackend.whenGET('http://localhost:8100/notauthenticated')
        .respond(401, {message: "Not Authenticated"});
  $httpBackend.whenGET('http://localhost:8100/notauthorized')
        .respond(403, {message: "Not Authorized"});
 
  $httpBackend.whenGET(/templates\/\w+.*/).passThrough();
 })