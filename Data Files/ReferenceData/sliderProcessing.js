var x = document.getElementsByClassName('beer-slider beer-slider-wlabels')
var i;
for (i = 0; i < x.length; i++) { 
  new BeerSlider(document.getElementById(x[i].getAttribute('id')));
}