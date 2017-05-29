/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

let currentFile;
let filterredFile;

let loadFile = (event) => {
    let file = event.target.files[0];
    var reader = new FileReader();
    reader.onload =  (e) => {
        currentFile = e.target.result;
        filterredFile = currentFile;
        $('.preview-image').attr('src', e.target.result);
    };
    reader.readAsDataURL(file);
};

function filterImage(f) {
    $.ajax({
        contentType: 'application/json',
        type: "POST",
        url: `api/image/render`,
        data: JSON.stringify({
            user_id: $.cookie("user_id"),
            image: currentFile,
            filter: f
        }),
        success    : function(data){
            //currentFile = data;
            filterredFile = 'data:image/jpeg;base64,' + data;
            $('.preview-image').attr('src', filterredFile);
        }
    });
};


let clearFilter = () => {
    filterredFile = currentFile;
    $('.preview-image').attr('src', currentFile);
}

let uploadImage = () => {
    let imgContent = {
        user_id: $.cookie("user_id"),
        image: filterredFile,
        description: $('.img-content').val()
    };
    $.ajax({
        contentType: 'application/json',
        type: "POST",
        url: `api/image/upload`,
        data: JSON.stringify(imgContent),
        success    : (data) => {
            location.reload();
        }
    });
};


let loadImage = () => {
    $.ajax({
    type: "GET",
    url: `api/image/all`,
    success    : (res) => {
        let strView = "";
        res = res.sort((a, b) => {
            return b.id - a.id;
        })
        res.forEach(img => {
            strView += `
                <div class="col-md-6 col-md-offset-3 card" style=" background-color: white;  border-radius: 2px">
                    <!-- header -->
                    <div class="col-md-12 card-title" style="height: 48px; border-bottom: solid 1px #ccc">
                        <div style="margin-top: 12px;  ">
                            <img id="" src="${img.user_avatar}" width="24px"  height="24px" style="border-radius: 50%;" />
                            <span style="font-size: 14px">${img.user_name}</span>
                        </div>
                    </div>
                     <div zclass="col-md-12 card-image" >
                        <a href="imageDetail.zul">
                            <img 
                                id="" 
                                src="img/${img.user_id}/${img.file_name}" 
                                style="width: 100%; "/>
                        </a>
                    </div>
                    <div class="col-md-12 card-footer" style="border-top: 0px">
                        <div style="margin-top: 12px; font-size: 15px; ">
                            <p>${img.description}</p>
                        </div>
                    </div>
                    <div class="col-md-12 card-footer" style="height: 48px; border-top: 0px">
                        <div style="margin-top: 12px; font-size: 15px; ">
                            <n:i class="fa fa-heart" aria-hidden="true" xmlns:n="native"></n:i> 100
                            <n:i class="fa fa-comment-o" aria-hidden="true" xmlns:n="native"></n:i> 100
                        </div>
                    </div>
                    <div  class="col-md-12" style="border-top: 0px; font-size: 14px; margin-bottom: 10px">
                        <div class="col-md-12" style="margin-top: 10px">
                            <div class="col-md-1">
                                <image id="" src="./img/avatar.jpg" width="24px"  height="24px" style="border-radius: 50%;" />
                            </div>
                            <div class="col-md-11">
                                <n:b xmlns:n="native"> vo_phi_hung</n:b> this is a comment of this image <n:br xmlns:n="native"/>
                                22 hours ago <n:span xmlns:n="native" style="color: #888; font-size: 12px">reply</n:span>
                            </div>
                        </div>
                        <div class="col-md-12" style="margin-top: 10px">
                            <div class="col-md-1">
                                <image id="" src="./img/avatar.jpg" width="24px"  height="24px" style="border-radius: 50%;" />
                            </div>
                            <div class="col-md-11">
                                <n:b xmlns:n="native"> vo_phi_hung</n:b> this is a comment of this image <n:br xmlns:n="native"/>
                                22 hours ago <n:span xmlns:n="native" style="color: #888; font-size: 12px">reply</n:span>
                            </div>
                        </div>
                        <div class="col-md-12" style="margin-top: 10px">
                            <div class="col-md-1">
                                <image id="" src="./img/avatar.jpg" width="24px"  height="24px" style="border-radius: 50%;" />
                            </div>
                            <div class="col-md-11">
                                <n:b xmlns:n="native"> vo_phi_hung</n:b> this is a comment of this image <n:br xmlns:n="native"/>
                                22 hours ago <n:span xmlns:n="native" style="color: #888; font-size: 12px">reply</n:span>
                            </div>
                        </div>
                    </div>
                </div>
            `;
        })
        
        
        $('.view-list').html(strView) 
        //render data
    }
    });
}
setTimeout(() => {
        loadImage();
}, 1000)
