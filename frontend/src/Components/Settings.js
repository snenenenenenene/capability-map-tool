import axios from 'axios';
import React, {Component} from 'react';
import { Link } from 'react-router-dom';

export default class Admin extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            environments: {}
            };
    }

    async componentDidMount() {
    }

    render() {
        return(
            <div>
            <nav aria-label="breadcrumb">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item"><Link to={`/home`}>Home</Link></li>
                    <li className="breadcrumb-item">Settings</li>
                </ol>
            </nav>
            <div className="jumbotron">
                    <div className="card-deck">
                        <div className="card">
                            <div className="card-body">
                                <h4 className="card-title text-center">Settings</h4>
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
                    </div>
                </div>
            </div>
        )
    }
}