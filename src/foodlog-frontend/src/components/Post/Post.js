import React, {useState, useEffect} from "react";

function Post() {
    const [error, setError] = useState(null);
    const [isLoaded, setIsLoaded] = useState(false);
    const [postList, setPostList] = useState([]);

    useEffect(() => {
        fetch("/posts")
            .then(res => res.json())
            .then(
                (result) => {
                    setIsLoaded(true);
                    setPostList(result);
                },
                (error) => {
                    setIsLoaded(true);
                    setError(error);
                    console.log(error);
                }
            )
    }, []);

    if(error) {
        return <div>Error!!!</div>;
    } else if(!isLoaded) {
        return <div>Loading...</div>
    } else {
        return(
            <ul>
                {postList.map(post => (
                    <li>
                        {post.title} {post.text}
                        <div>
                            <div>
                                {post.imagePaths.map(imagePath => (
                                    <img src="https://picsum.photos/400" alt={imagePath} width="200" height="200" />
                                ))}
                            </div>
                            <div>
                                <video width="300" height="300" controls title={post.shortVideoPath}>
                                    <source src="https://media.istockphoto.com/videos/speaker-businessman-talking-at-webcam-making-conference-video-call-video-id1158583412" type="video/mp4" />
                                    Your browser does not support the video tag.
                                </video>
                            </div>
                        </div>
                    </li>
                ))}
            </ul>
        );
    }
}

export default Post;
