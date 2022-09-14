import {
    AppBar,
    Button,
    Dialog,
    IconButton,
    Paper,
    Slide,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Toolbar,
    Typography
} from "@mui/material";
import CloseIcon from '@material-ui/icons/Close'
import Post from "../Post/Post";
import React, {useEffect, useState} from "react";
import {GetWithAuth} from "../../services/HttpService";

const columns = [
    {
        id: 'User Activity',
        label: 'User Activity',
        minWidth: 170,
        align: 'left',
        format: (value) => value.toLocaleString('en-US'),
    },
];

const Transition = React.forwardRef(function Transition(props, ref) {
    return <Slide direction="up" ref={ref} {...props} />;
});

function PopUp(props) {
    const {isOpen, setIsOpen, postId} = props;
    const [open, setOpen] = useState(isOpen);
    const [post, setPost] = useState(null);

    const getPost = () => {
        GetWithAuth("/posts/" + postId)
            .then(res => res.json())
            .then(
                (result) => {
                    console.log(result);
                    setPost(result);
                },
                (error) => {
                    console.log(error);
                }
            );
    }

    const handleClose = () => {
        setOpen(false);
        setIsOpen(false);
    };

    useEffect(() => {
        setOpen(isOpen);
    }, [isOpen]);

    useEffect(() => {
        getPost();
    }, [postId]);

    return (
        post != null ?
            <Dialog
                fullScreen
                open={open}
                onClose={handleClose}
                TransitionComponent={Transition}
            >
                <AppBar sx={{position: 'relative'}}>
                    <Toolbar>
                        <IconButton
                            edge="start"
                            color="inherit"
                            onClick={handleClose}
                            aria-label="close"
                        >
                            <CloseIcon/>
                        </IconButton>
                        <Typography sx={{ml: 2, flex: 1}} variant="h6" component="div">
                            Close
                        </Typography>
                    </Toolbar>
                </AppBar>
                <Post likes={post.postLikes} postId={post.id} userId={post.userId} userName={post.userName}
                      title={post.title} text={post.text}/>
            </Dialog>
            : "Loading..."
    );
}

function UserActivity(props) {
    const {userId} = props;
    const [error, setError] = useState(null);
    const [isLoaded, setIsLoaded] = useState(false);
    const [rows, setRows] = useState([]);
    const [isOpen, setIsOpen] = useState(false);
    const [selectedPost, setSelectedPost] = useState(null);

    const getActivity = () => {
        GetWithAuth("/users/activity/" + userId)
            .then(res => res.json())
            .then(
                (result) => {
                    setIsLoaded(true);
                    console.log(result);
                    setRows(result);
                },
                (error) => {
                    console.log("Error: " + error);
                    setIsLoaded(true);
                    setError(error);
                }
            );
    }

    const handleNotification = (postId) => {
        setSelectedPost(postId);
        setIsOpen(true);
    }

    useEffect(() => {
        getActivity();
    }, []);

    return (
        <div>
            {isOpen ? <PopUp isOpen={isOpen} postId={selectedPost} setIsOpen={setIsOpen}/> : ""}
            {isLoaded ? <Paper sx={{width: '100%', overflow: 'hidden', marginTop: 10}}>
                <TableContainer sx={{maxHeight: 440, maxWidth: 800, minWidth: 100}}>
                    <Table stickyHeader aria-label="sticky table">
                        <TableHead>
                            <TableRow>
                                User Activity
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {rows.map((row) => {
                                return (
                                    <Button onClick={() => handleNotification(row[1])}>
                                        <TableRow hover role="checkbox" tabIndex={-1} key={row.code}>
                                            <TableCell align='right'>
                                                {row[3] + " " + row[0] + " your post"}
                                            </TableCell>
                                        </TableRow>
                                    </Button>
                                );
                            })}
                        </TableBody>
                    </Table>
                </TableContainer>
            </Paper> : ""}
        </div>
    );
}

export default UserActivity;