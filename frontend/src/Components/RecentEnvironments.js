import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import RecentEnvironmentTableRow from "./RecentEnvironmentTableRow";
import axios from 'axios';

export default class Home extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            environments: []
        };
    }

    async componentDidMount() {
        await axios.get(`${process.env.REACT_APP_API_URL}/environment/all`)
            .then(response => this.setState({environments: response.data}))
            .catch(error => {
                this.props.history.push('/error')
            })
    }

    recentEnvironmentTableRow() {
        return this.state.environments.map((row, i) => {
            return <RecentEnvironmentTableRow obj={ row } key={ i }/>
        })
    }

    render() {
        return(
            <div>
                <nav aria-label="breadcrumb">
                    <ol className="breadcrumb">
                        <li className="breadcrumb-item"><Link to={`/home`}>Home</Link></li>
                        <li className="breadcrumb-item"><Link to={`/recent`}>Recent Environments</Link></li>
                    </ol>
                </nav>
                <div className="jumbotron">
                    <h1 className='display-4'>Recent Environments</h1>
                    <br/><br/>
                    <table className='table table-striped'>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        { this.recentEnvironmentTableRow() }
                        </tbody>
                    </table>
                </div>
            </div>
        )
    }








}