var app = angular.module('signup', []);

app.config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.xsrfCookieName = 'csrftoken';
    $httpProvider.defaults.xsrfHeaderName = 'X-CSRFToken';
}]);

app.controller('UserController', function($http) {
    var vm = this;

    vm.formInfo = {};

    vm.index = function() {
    }

    vm.show = function() {
    }

    vm.create = function() {
        // TODO:Validate the form data here.
        // 1. Check email format
        // 2. Check name
        // 3. password and confirm password must match
        //
        // Change the api to '/user/create/'. Then on creation check 
        // the type of user. i.e whether we are creating student or faculty
        $http.post('/add_student/', vm.formInfo)
        .then(function success(response) {
            console.log(response);
            toastr.success(" has been created");
        },
        function error(response) {
            console.log(response);
            toastr.warning("Failure : " + response.data.reason);
        });
    }

    vm.update = function() {
    }

    vm.delete = function() {
    }
});
