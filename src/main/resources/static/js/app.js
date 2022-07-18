$(function () {
  $('a.confirmDeletion').click(function () {
    if (!confirm('Confirm deletion')) return false;
  });
});

//form 이미지 미리보기
function readURL(input, idNum) {
  if (input.files && input.files[0]) {
    let reader = new FileReader();

    reader.onload = function (e) {
      $('#imgPreview' + idNum)
        .attr('src', e.target.result)
        .width(100)
        .height(100);
    };

    reader.readAsDataURL(input.files[0]);
  }
}
