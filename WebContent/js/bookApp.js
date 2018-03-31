var bookApp = angular.module('bookApp', []);
bookApp.controller('myController', ['$scope', function ($scope) {
    $scope.hey = "hi";
}]);

bookApp.controller('bookController', ['$scope', '$http', function ($scope, $http) {
    $scope.booklistView ={
        All: false,
        Purchased: false,
        singleBook: false
    };

    $scope.allbooks = function(){
        $scope.booklistView.All = true;
        $http.get("BooksServlet/getAllBooks").then(function (response) {
            $scope.booklist = response.data;
            $scope.booklistView.All = true;
        },
        function (response) {});
    };
    $scope.purchasedbooks = function(){
        //only logged

    };
    $scope.singlebook = function(chosenBook){

    };
}]);

bookApp.controller('loginController', ['$scope', '$http', function ($scope, $http) {
    $scope.loginMode = false;
    $scope.registerMode = false;
    $scope.loginFormErrorToggle = false;
    $scope.loggedUser = "";
    $scope.adminLogged = false;

    $scope.isLogged = isLoggedFunc();

    function isLoggedFunc() {
        $http.get("LoginServlet").then(function (response) {
                $scope.isLogged = true;
                $scope.loggedUser = response.data.username;
                if ("isAdmin" in response.data) {
                    $scope.adminLogged = true;
                }
            },
            function (response) {});
    }

    $scope.loginFunc = function () {
        if ($scope.loginForm.$invalid){
            return;
        }
        var obj = {
            Username: $scope.login.User,
            Password: $scope.login.Password
        };

        $http.post("LoginServlet", obj).then(function (response) {
            $scope.loginFormErrorToggle = false;
            // $scope.loginUsernameErrorToggle = false;
            // $scope.loginUsernameError = "";
            // $scope.loginPasswordErrorToggle = false;
            // $scope.loginPasswordError = "";
            $scope.loggedUser = $scope.login.User;
            $scope.isLogged = true;
            if (angular.equals(obj.Username, "admin")) {
                // admin login successful
                $scope.adminLogged = true;
            }
            // upon succsess
            $scope.loginMode = false;
        }, function (reposnse) {
            $scope.loginFormErrorToggle = true;
            $scope.loginFormError = "Wrong username or password";
            $scope.loggedUser = "";
            $scope.isLogged = false;
            return;
        });
    };

    $scope.registerFunc = function () {
        console.log("do register");
        if ($scope.registerForm.$invalid){
            return;
        }
        var obj = {
            Username: $scope.register.User,
            Password: $scope.register.Password,
            Nickname: $scope.register.NickName,
            Email: $scope.register.Email,
            Phone: $scope.register.Telephone.Area + "" + $scope.register.Telephone.Full,
            Address: $scope.register.Address.Street + " " +
                $scope.register.Address.City + " " +
                $scope.register.Address.zip,
            Description: $scope.register.About,
            Photo: $scope.register.photo
        };

        $http.post("RegistrationServlet", obj).then(function (response) {
            $scope.RegisterFormErrorToggle = false;

            $scope.loggedUser = $scope.register.User;
            $scope.isLogged = true;
            $scope.registerMode = false;

            // upon succsess
            $scope.loginMode = false;
        }, function (reposnse) {
            $scope.RegisterFormErrorToggle = true;
            $scope.registerFormError = "Error filling form";
            $scope.loggedUser = "";
            $scope.isLogged = false;
            return;
        });
        //upon success
    };
    $scope.openLogin = function () {
        $scope.loginMode = !$scope.loginMode;
    };
    $scope.openRegister = function () {
        $scope.registerMode = !$scope.registerMode;
        console.log("register mode active");
    };
}]);


// jQuery stuff

//support for tooltips (from w3schools)
// $(document).ready(function(){
//     $('[data-toggle="tooltip"]').tooltip();
// });