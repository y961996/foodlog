import {Avatar, Button, CardContent, InputAdornment, OutlinedInput} from "@mui/material";
import {makeStyles} from "@mui/styles"
import {createTheme} from "@mui/material/styles";
import {Link, useNavigate} from "react-router-dom";
import {useState} from "react";
import {PostWithAuth, RefreshToken} from "../../services/HttpService";

const theme = createTheme();

const useStyles = makeStyles(() => ({
    comment: {
        display: "flex", flexWrap: "wrap", justifyContent: "flex-start", alignItems: "center",
    }, small: {
        width: theme.spacing(4), height: theme.spacing(4),
    }, link: {
        textDecoration: "none", boxShadow: "none", color: "white",
    }
}));

function CommentForm(props) {
    const {userId, userName, postId, setCommentRefresh} = props;
    const classes = useStyles();
    const [text, setText] = useState("");

    let navigate = useNavigate();

    const logout = () => {
        localStorage.removeItem("tokenKey");
        localStorage.removeItem("refreshKey");
        localStorage.removeItem("currentUser");
        localStorage.removeItem("userName");
        navigate(0, { replace: true });
    }

    const saveComment = () => {
        PostWithAuth("/comments", {
            postId: postId,
            userId: userId,
            text: text,
        })
            .then((res) => {
                if(!res.ok) {
                    RefreshToken()
                        .then((res) => {
                            if(!res.ok) {
                                logout();
                            }else {
                                return res.json();
                            }
                        })
                        .then((result) => {
                            if(result !== undefined) {
                                localStorage.setItem("tokenKey", result.accessToken);
                                saveComment();
                                setCommentRefresh();
                            }
                        })
                        .catch((err) => console.log("Error:" + err));
                } else {
                    return res.json();
                }
            })
            .catch((err) => console.log("Error: " + err))
    }

    const handleSubmit = () => {
        saveComment();
        setText("");
        setCommentRefresh();
    }

    const handleChange = (value) => {
        setText(value);
    }

    return (<CardContent className={classes.comment}>
        <OutlinedInput
            id="outlined-adornment-amount"
            multiline
            inputProps={{maxLength: 250}}
            fullWidth
            onChange={(i) => handleChange(i.target.value)}
            startAdornment={<InputAdornment position="start">
                <Link className={classes.link} to={{pathname: '/users/' + userId}}>
                    <Avatar aria-label="recipe" className={classes.small}>
                        {userName.charAt(0).toUpperCase()}
                    </Avatar>
                </Link>
            </InputAdornment>}
            endAdornment={<InputAdornment position="end">
                <Button
                    variant="contained"
                    style={{
                        background: 'linear-gradient(45deg, #2196F3 30%, #21CBF3 90%)', color: "white"
                    }}
                    onClick={handleSubmit}
                >
                    Comment
                </Button>
            </InputAdornment>}
            value={text}
            style={{color: "black", backgroundColor: "white"}}
        >
        </OutlinedInput>
    </CardContent>);
}

export default CommentForm;