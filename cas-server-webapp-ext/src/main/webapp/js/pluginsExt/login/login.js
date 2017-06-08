/**
 * Created by dell on 2016/10/26.
 */

$(function(){
    var msg=document.getElementById('msg');
    console.log(msg);
    if(msg){
        var $errorText=$('#msg').text();
        $('.msg-error').show().siblings().hide();
        $('.msg-error').html('<b class="fa fa-minus-circle"></b>&nbsp;&nbsp;'+$errorText+'').show();
    }
})
$(function(){
    $('#codeQuick').click(function(){
        showlogin($(this),$('.acc-login'))
        $('.msg-tips').hide().siblings().hide();
    });

    $('.static').click(function(){
        showlogin($(this),$('.qrcode-login'))
        $('.msg-tips').show();
    });
    function showlogin(dom,obj){
        dom.children().attr('class','active');
        dom.siblings().children().attr('class','grayfont');
        obj.hide().siblings().show();
    };
    $('.footer').load('footer.html');
})
    //清除input值
    $('input.form-control').on('click',function(){
        $(this).next().show();
        $(this).next().on('click',function(event){
            event.stopPropagation();
            $(this).prev().val('');
            $(this).hide();
        })
    });
    function login(){
        var $user=$('.userName');
        var $pwd=$('.pwd');
        var checkInput = function (str) {
            var pattern = pattern = /^[\w\u4e00-\u9fa5]+$/gi;
            if(pattern.test(str)){
                return false;
            }
            return true;
        }
        if($user.val()==""||$pwd.val()==""){
            $('.msg-error').html('<b class="fa fa-minus-circle"></b>&nbsp;&nbsp;账户名或密码不能为空')
            $('.msg-error').show().siblings().hide();
            $user.focus();
            return false;
        }
        
        //if(checkInput($user.val())||checkInput($pwd.val())){
        //    $('.msg-error').html('<b class="fa fa-minus-circle"></b>&nbsp;&nbsp;格式错误')
        //    $('.msg-error').show().siblings().hide();
        //    $user.focus();
        //    return false;
        //}
        
        var pubKey = $('#pubKey').val();
	    var encrypt = new JSEncrypt();
	    encrypt.setPublicKey(pubKey);
	    var pwd = encrypt.encrypt($pwd.val());
	    $pwd.val(pwd);
        //if
    }