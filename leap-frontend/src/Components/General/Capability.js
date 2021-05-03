import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import RecentTableRow from './RecentEnvironmentTableRow'
import RecentEnvironmentTableRow from "./RecentEnvironmentTableRow";
import CapabilityTableRow from "./CapabilityTableRow";


export default class Home extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            capabilities: []
        };
    }

    componentDidMount() {
    }

    capabilityTable() {
        return this.state.capabilities.map((row, i) => {
            return <CapabilityTableRow obj={ row } key={ i }/>
        })
    }

    render() {
        return(
            <div className="jumbotron">
                <h1 className='display-4'>Capabilities</h1>
                <br/><br/>
                <table className='table table-striped'>
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Description</th>
                    </tr>
                    </thead>
                    <tbody>
                    { this.capabilityTable() }
                    </tbody>
                </table>
            </div>
        )
    }








}