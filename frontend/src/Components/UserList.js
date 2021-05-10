import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import UsersTableRow from "./UsersTableRow";


export default class Home extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            users: []
        };
    }

    async componentDidMount() {
        await fetch(`http://localhost:8080/api/user/all`)
            .then(resp => resp.json())
            .then(data => {
                this.setState({users: data});
            })
            .catch(error => {
                this.props.history.push('/error')
            })
    }

    usersTableRow() {
        return this.state.users.map((row, i) => {
            return <UsersTableRow obj={ row } key={ i }/>
        })
    }

    render() {
        return(
            <div>
            <nav aria-label="breadcrumb">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item"><Link to={`/home`}>Home</Link></li>
                    <li className="breadcrumb-item"><Link to={`/users`}>User List</Link></li>
                </ol>
            </nav>
            <div className="jumbotron">
                <h1 className='display-4'>User List</h1>
                <br/><br/>
                <table className='table table-striped'>
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Role</th>
                    </tr>
                    </thead>
                    <tbody>
                    { this.usersTableRow() }
                    </tbody>
                </table>
            </div>
            </div>
        )
    }








}