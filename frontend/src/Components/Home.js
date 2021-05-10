import React, {Component} from 'react';
import { Link } from 'react-router-dom';

export default class Home extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            environments: {}
            };
    }

    async componentDidMount() {
        await fetch(`http://localhost:8080/api/environment/all`)
            .then(resp => resp.json())
            .then(data => {
                this.setState({environments: data});
            })
            .catch(error => {
                this.props.history.push('/error')
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
                                        <Link to={'/users'}>
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