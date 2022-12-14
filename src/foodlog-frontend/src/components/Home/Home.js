import React, {useEffect, useState} from "react";
import Post from "../Post/Post";
import "./Home.scss";
import PostForm from "../Post/PostForm";
import {GetWithoutAuth} from "../../services/HttpService";

function Home() {
    const [error, setError] = useState(null);
    const [isLoaded, setIsLoaded] = useState(false);
    const [postList, setPostList] = useState([]);

    const refreshPosts = () => {
        GetWithoutAuth("/posts")
            .then(res => {
                responseClone = res.clone();
                return res.json();
            })
            .then(
                (result) => {
                    setIsLoaded(true);
                    setPostList(result);
                },
                (error) => {
                    setIsLoaded(true);
                    setError(error);
                    console.log(error, responseClone);
                    responseClone.text()
                        .then(bodyText => {
                            console.log("Received: " + bodyText);
                        })
                }
            )
    }

    // TODO: This is an example of how to get the response when you got an error
    //       Maybe put this in a method and use that to make requests.
    let responseClone;
    useEffect(() => {
        refreshPosts();
    }, []);

    if (error) {
        return <div>Error!!!</div>;
    } else if (!isLoaded) {
        return <div>Loading...</div>
    } else {
        return (
            //TODO: This container doesn't work properly. (It won't expand with the posts and
            //      posts don't have margins between them.
            <div className="container">
                {localStorage.getItem("currentUser") === null ? "" :
                    <PostForm userId={localStorage.getItem("currentUser")}
                              userName={localStorage.getItem("userName")}
                              refreshPosts={refreshPosts}/>}
                {postList.map(post => (
                    <Post
                        likes={post.postLikes}
                        postId={post.id}
                        userId={post.userId}
                        userName={post.userName}
                        title={post.title}
                        text={post.text}
                        imagePaths={post.imagePaths}
                        shortVideoPath={post.shortVideoPath}
                    />
                ))}
            </div>
        );
    }
}

export default Home;