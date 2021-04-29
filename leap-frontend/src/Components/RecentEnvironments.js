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

    componentDidMount() {
    }

    recentEnvironmentTableRow() {
        return this.state.environments.map((row, i) => {
            return <RecentEnvironmentTableRow obj={ row } key={ i }/>
        })
    }

    render() {
        return(
            <div className="jumbotron">
                <h1 className='display-4'>Recent Environments</h1>
                <br/><br/>
                <table className='table table-striped'>
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Description</th>
                    </tr>
                    </thead>
                    <tbody>
                    { this.recentEnvironmentTableRow() }
                    </tbody>
                </table>
            </div>
        )
    }








}