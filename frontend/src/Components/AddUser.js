import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import axios from 'axios';
import toast from 'react-hot-toast';

export default class AddUser extends Component {
    constructor(props) {
        super(props);
        this.state = {
            roles: [],
            username: '',
            roleId: 1,
            email: '',
        };
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleInputChange = this.handleInputChange.bind(this)
    }

    handleSubmit = async e => {
        e.preventDefault();
        const formData = new FormData()
        formData.append("username",this.state.username)
        formData.append("email",this.state.email)
        formData.append("password","newUser")
        formData.append("roleId",this.state.roleId)
        await axios.post(`${process.env.REACT_APP_API_URL}/user/register`, formData)
        .then(response => toast.success("User Added Successfully!"))
        .catch(error => toast.error("Could not Add User"))
        this.props.history.push(`/user`)
    }

    async componentDidMount() {
        await axios.get(`${process.env.REACT_APP_API_URL}/role/`)
        .then(response => this.setState({statuses: response.data}))
        .catch(error => {
            toast.error("Could not Load Roles")
        })
    }

    handleInputChange(event) {
        this.setState({ [event.target.name]: event.target.value })
    }

    roleListRows() {
        return this.state.roles.map((role) => {
            return <option key={role.roleId} value={role.roleId}>{role.roleName}</option>
        })
    }

    render() {
        return (
            <div>
                <nav aria-label="breadcrumb">
                    <ol className="breadcrumb">
                        <li className="breadcrumb-item"><Link to={`/`}>Home</Link></li>
                        <li className="breadcrumb-item active" aria-current="page">Add Capability</li>
                    </ol>
                </nav>
            <div className="jumbotron">
                <h3>Add User</h3>
                <form onSubmit={this.handleSubmit}>
                    <div className="row">
                        <div className="col-sm">
                            <div className="form-row">
                                <div className="form-group col-md-6">
                                    <label htmlFor="username">Name</label>
                                <input type="text" id="username" name="username" className="form-control" placeholder="Username"
                                       value={this.state.username} onChange={this.handleInputChange}/>
                                </div>
                            </div>
                            <div className="form-row">
                            <div className="form-group col-md-6">
                                    <label htmlFor="email">Email</label>
                                <input type="email" id="email" name="email" className="form-control" placeholder="Email"
                                       value={this.state.email} onChange={this.handleInputChange}/>
                                </div>
                            </div>
                            <div className="form-row">
                            <div className="form-group col-md-6">
                                        <label htmlFor="paceOfChange">Role</label>
                                        <select className="form-control" name="roleId" id="roleId" placeholder="Role"
                                                value={this.state.roleId} onChange={this.handleInputChange}>
                                            <option key="1" value={1}>User</option>
                                            <option key="2" value={2}>Admin</option>
                                            {/* {this.roleListRows()} */}
                                        </select>
                                    </div>
                            </div>
                        </div>
                    </div>
                    <button className="btn btn-primary" type="button" onClick={this.handleSubmit}>Submit</button>
                </form>
            </div>
        </div>
        )
    }
}