import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import RecentTableRow from './RecentEnvironmentTableRow'
import RecentEnvironmentTableRow from "./RecentEnvironmentTableRow";


export default class Home extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            environments: []
        };
    }

    async componentDidMount() {
        const response = await fetch(`http://localhost:8080/environment/all`);
        const data = await response.json();
        this.setState({environments: data});
        console.log(this.state.environments);
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
                    <li className="breadcrumb-item"><Link to={`/home`}><a>Home</a></Link></li>
                    <li className="breadcrumb-item"><Link to={`/recent`}><a>Recent Environments</a></Link></li>
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