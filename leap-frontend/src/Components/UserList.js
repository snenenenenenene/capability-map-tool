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

    componentDidMount() {
    }

    usersTableRow() {
        return this.state.users.map((row, i) => {
            return <UsersTableRow obj={ row } key={ i }/>
        })
    }

    render() {
        return(
            <div>
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
        )
    }








}