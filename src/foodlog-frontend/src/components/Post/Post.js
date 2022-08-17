import "./Post.scss"

function Post(props) {
    const {title, text, imagePaths, shortVideoPath} = props;

    return(
        <div className="postContainer">
            {title}
            {text}
            {imagePaths}
            {shortVideoPath}
        </div>
    );
}

export default Post;
