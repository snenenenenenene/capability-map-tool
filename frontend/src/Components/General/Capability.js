import React, {Component} from 'react';
import { Link } from 'react-router-dom';
import CapabilityHierarchy from './CapabilityHierarchy';


export default class Capability extends Component
{
    constructor(props) {
        super(props);
        this.state = {
            environments: [],
            environmentName: this.props.match.params.name,
            environmentId: '',
            capabilities: []
        };
    }

    async componentDidMount() {
        const environmentResponse = await fetch(`${process.env.REACT_APP_API_URL}/environment/environmentname/${this.state.environmentName}`);
        const environmentData = await environmentResponse.json();
        this.setState({environmentId: environmentData.environmentId});

        const capabilityResponse = await fetch(`${process.env.REACT_APP_API_URL}/capability/getallbyenvironment/${this.state.environmentId}`);
        const capabilityData = await capabilityResponse.json();
        capabilityData.forEach((cap) => {
            cap["id"] = cap.capabilityId
            cap["name"] = cap.capabilityName
            cap["parentId"] = cap.parentCapabilityId
        })
        capabilityData[0].parentId = null;
        this.setState({capabilities: capabilityData});
        console.log(this.state.capabilities)
    }

    render() {
        const environmentName = this.props.match.params.name;
        return(
            <div className="jumbotron">
                <nav aria-label="breadcrumb">
                    <ol className="breadcrumb">
                        <li className="breadcrumb-item"><Link to={`/`}>Home</Link></li>
                        <li className="breadcrumb-item"><Link to={`/environment/${environmentName}`}>{environmentName}</Link></li>
                        <li className="breadcrumb-item">Capabilities</li>
                    </ol>
                </nav>
                <h1 className='display-4'>Capabilities</h1>
                <br/><br/>
                <div className="row">
                    <CapabilityHierarchy capabilities={this.state.capabilities}/>
                    </div>
                        <Link to={'edit'}>
                            <input type="button" value="Edit" className="btn btn-secondary input-button hoverable"/>
                        </Link>
                </div>
        )
    }








}