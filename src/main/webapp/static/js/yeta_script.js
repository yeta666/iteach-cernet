
(function() {
    "use strict";

   // Menu Toggle
   jQuery('.toggle-btn').click(function(){
      var body = jQuery('body');
      var bodyposition = body.css('position');
      if(bodyposition != 'relative') {
         if(!body.hasClass('left-side-collapsed')) {
            body.addClass('left-side-collapsed');
            jQuery(this).addClass('menu-collapsed');
         } else {
            body.removeClass('left-side-collapsed chat-view');
            jQuery(this).removeClass('menu-collapsed');
         }
      } else {
         if(body.hasClass('left-side-show'))
            body.removeClass('left-side-show');
         else
            body.addClass('left-side-show');
      }
   });
   
   jQuery(window).resize(function(){
      if(jQuery('body').css('position') == 'relative') {
         jQuery('body').removeClass('left-side-collapsed');
      } else {
         jQuery('body').css({left: '', marginRight: ''});
      }
   });

})(jQuery);