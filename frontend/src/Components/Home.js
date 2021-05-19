import axios from 'axios';
import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import toast from 'react-hot-toast';
export default class Home extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            environments: {}
            };
    }

    async componentDidMount() {
        await axios.get(`${process.env.REACT_APP_API_URL}/environment/`)
            .then(response => this.setState({environments: response.data}) )
            .catch(error => {
                toast.error("Could not Connect to Backend!")
            })
    }

    render() {
        return(
            <div>
            <nav aria-label="breadcrumb">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item"><Link to={`/home`}>Home</Link></li>
                </ol>
            </nav>
            <div className="jumbotron">
                    <div className="card-deck">
                        <div className="card">
                            <div className="card-body">
                                <h4 className="card-title text-center">User List</h4>
                            </div>
                            <div className="card-footer">
                                <form>
                                    <div className="text-center">
                                        <Link to={'/user'}>
                                            <input type="button" value="User list" className="input-button hoverable"/>
                                        </Link>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-body">
                                <h4 className="card-title text-center">Environment</h4>
                            </div>
                            <div className="card-footer">
                                <form>
                                    <div className="text-center">
                                        <Link to={'/add'}>
                                            <input type="button" value="New Environment" className="input-button hoverable"/>
                                        </Link>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-body">
                                <h4 className="card-title text-center">Recently used Environments</h4>

                            </div>
                            <div className="card-footer">
                                <form>
                                    <div className="text-center">
                                        <Link to={'/recent'}>
                                            <input type="button" value="Recently Used Environments" className="input-button hoverable"/>
                                        </Link>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
            </div>
            </div>
        )
    }








}