import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import CapabilityTableRow from "./CapabilityTableRow";


export default class Capability extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            environments: [],
            environmentName: '',
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
        const environmentName = this.props.match.params.name;
        return(
            <div className="jumbotron">
                <nav aria-label="breadcrumb">
                    <ol className="breadcrumb">
                        <li className="breadcrumb-item"><Link to={`/`}><a>Home</a></Link></li>
                        <li className="breadcrumb-item"><Link to={`/environment/${environmentName}`}><a>{environmentName}</a></Link></li>
                    </ol>
                </nav>
                <h1 className='display-4'>Capabilities</h1>
                <br/><br/>
                <div className="row">
                    <div className="col-sm-9">
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
                    <div className="col-sm-2">

                    <div className="text-center">
                        <Link to={'edit'}>
                            <input type="button" value="Edit" className="btn btn-secondary input-button hoverable"/>
                        </Link>
                    </div>
                        <br/>
                    <div className="text-center">
                        <Link to={'childcapability'}>
                            <input type="button" value="Child Capability" className="btn btn-secondary input-button hoverable"/>
                        </Link>
                    </div>
                    </div>
                </div>
            </div>
        )
    }








}