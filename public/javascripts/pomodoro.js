var pomodoro = {
    started : false,
    minutes : 0,
    seconds : 0,
    fillerHeight : 0,
    fillerIncrement : 0,
    interval : null,
    minutesDom : null,
    secondsDom : null,
    fillerDom : null,
    init : function(){
      var self = this;
      this.minutesDom = $('#minutes');
      this.secondsDom = $('#seconds');
      this.fillerDom = $('#filler');
      this.interval = setInterval(function(){
        self.intervalCallback.apply(self);
      }, 1000);
      $('#work').click(function(){
        self.startWork.apply(self);
      });
      $('#stop').click(function(){
        self.stopTimer.apply(self);
      });
    },
    resetVariables : function(mins, secs, started){
      this.minutes = mins;
      this.seconds = secs;
      this.started = started;
      this.fillerIncrement = 200/(this.minutes*60);
      this.fillerHeight = 0;
    },
    startWork: function() {
      this.resetVariables(1, 0, true);
    },
    stopTimer : function(){
      this.resetVariables(25, 0, false);
      this.updateDom();
    },
    toDoubleDigit : function(num){
      if(num < 10) {
        return "0" + parseInt(num, 10);
      }
      return num;
    },
    updateDom : function(){
      this.minutesDom.text(this.toDoubleDigit(this.minutes));
      this.secondsDom.text(this.toDoubleDigit(this.seconds));
      this.fillerHeight = this.fillerHeight + this.fillerIncrement;
      this.fillerDom.css('height', this.fillerHeight + 'px');
    },
    intervalCallback : function(){
      if(!this.started) return false;
      if(this.seconds == 0) {
        if(this.minutes == 0) {
          this.timerComplete();
          return;
        }
        this.seconds = 59;
        this.minutes--;
      } else {
        this.seconds--;
      }
      this.updateDom();
    },
    timerComplete : function(){
      this.started = false;
      this.fillerHeight = 0;
    }
};
$(document).ready(function(){
  pomodoro.init();
});


